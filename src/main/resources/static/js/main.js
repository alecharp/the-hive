(function () {
  const max_zoom = 7;
  const hive = L.map('content').setView([48.85, 2.35], 3);
  L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: max_zoom,
    minZoom: 3,
    attribution: '&copy;&nspar;<a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    continuousWorld: true
  }).addTo(hive);

  const addBeeToMap = function (bee) {
    L.marker([bee.latitude, bee.longitude])
      .bindPopup(`<b>${bee.name}</b>`)
      .addTo(hive);
  };

  const zoom2Bee = function (bee) {
    hive.flyTo(L.latLng(bee.latitude, bee.longitude), max_zoom);
  };

  $.get('/api/hive', function (bees) {
    bees.forEach(addBeeToMap);
  });

  const modal = $('.ui.modal');
  modal.modal({
    onApprove: function () {
      return false;
    },
    onDeny: function () {
      $('.ui.form').form('reset');
    }
  });
  $('button#create-bee').click(function (event) {
    event.preventDefault();
    modal.modal('show');
    geoCoding();
  });

  /**
   * Define the acceptable boundaries of a number input.
   *
   * Syntax is minValue:maxValue.
   */
  $.fn.form.settings.rules.boundaries = function (value, boundaries) {
    const [minValue, maxValue] = boundaries.split(':');
    return Number(value) >= Number(minValue) && Number(value) <= Number(maxValue);
  };

  /**
   * Verify that one of the options from the suggested locations on the dropdowm menu has been selecting input before submitting the form
   *
   */
  let geometry;
  $.fn.form.settings.rules.geometry = function () {
    return geometry;
  };

  const form = $('.ui.form');
  form.form({
    inline: true,
    onSuccess: function (e, fields) {
      if (e !== undefined) e.preventDefault();
      $('.ui.form').addClass('loading');

      $.ajax({
        type: 'POST',
        url: '/api/bee',
        data: JSON.stringify(fields),
        contentType: 'application/json'
      })
        .done(function (bee) {
          form.form('reset');
          modal.modal('hide');
          addBeeToMap(bee);
          zoom2Bee(bee);
        })
        .fail(function () {
          form.form('set error');
        })
        .always(function () {
          form.removeClass('loading');
        });
    },
    fields: {
      name: {
        rules: [{
          type: 'empty',
          prompt: 'You must specify your name'
        }]
      },
      email: {
        rules: [{
          type: 'empty',
          prompt: 'You must specify your email'
        }, {
          type: 'email',
          prompt: 'Specified email is not valid'
        }]
      },
      latitude: {
        rules: [{
          type: 'empty',
          prompt: 'You must specify the latitude of your location'
        }, {
          type: 'number',
          prompt: 'The latitude must be a decimal number'
        }, {
          type: 'boundaries[-90:90]',
          prompt: 'The latitude must be higher than -90 and less than 90'
        }]
      },
      longitude: {
        rules: [{
          type: 'empty',
          prompt: 'You must specify the longitude of your location'
        }, {
          type: 'number',
          prompt: 'The longitude must be a decimal number'
        }, {
          type: 'boundaries[-180:180]',
          prompt: 'The latitude must be higher than -180 and less than 180'
        }]
      },
      location: {
        rules: [{
          type: 'geometry',
          prompt: 'A place must be selected from the suggestions on the dropdown list'
        }]
      }
    }
  });

  $('button#validate-form').click(function (event) {
    event.preventDefault();
    form.form('validate form');
    geometry = false;
  });

  const locationButton = $('.ui.button#location');
  if (!("geolocation" in navigator)) {
    locationButton.toggleClass('disabled');
  }
  locationButton.click(function (event) {
    event.preventDefault();
    locationButton.toggleClass('loading');
    navigator.geolocation.getCurrentPosition(function (position) {
      form.form('set values', {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
      });
      geometry = true;
      locationButton.toggleClass('loading');
    }, function () {
      locationButton.toggleClass('loading');
      locationButton.toggleClass('disabled');
    })
  });

  const geoCoding = function () {
    let autocomplete = new google.maps.places.Autocomplete(document.getElementById('location_input'), {regions: 'locality'});
    autocomplete.addListener('place_changed', function () {
      geometry = true;
      const place = autocomplete.getPlace();
      form.form('set values', {
        latitude: place.geometry.location.lat(),
        longitude: place.geometry.location.lng(),
      });
    });
  }

})();
