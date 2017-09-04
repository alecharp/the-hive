(function () {
  const hive = L.map('content').setView([48.85, 2.35], 3);
  L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 7,
    minZoom: 3,
    attribution: '&copy;&nspar;<a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    continuousWorld: true
  }).addTo(hive);

  const addBeeToMap = function (bee) {
    L.marker([bee.latitude, bee.longitude])
      .bindPopup(`<b>${bee.name}</b>`)
      .addTo(hive);
  };
  $.get('/api/hive', function(bees) {
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
  $('button#create-bee').click(function(event) {
    event.preventDefault();
    modal.modal('show');
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
          hive.panTo([fields.latitude, fields.longitude]);
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
      }
    }
  });
  $('button#validate-form').click(function(event) {
    event.preventDefault();
    form.form('validate form');
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
      locationButton.toggleClass('loading');
    }, function () {
      locationButton.toggleClass('loading');
      locationButton.toggleClass('disabled');
    })
  });
})();
