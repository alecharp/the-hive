(function () {
  const hive = L.map('content').setView([48.85, 2.35], 3);
  L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 7,
    minZoom: 3,
    attribution: '&copy;&nspar;<a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    continuousWorld: true
  }).addTo(hive);

  /**
   * Method to add a Bee on the map.
   *
   * @param bee a Bee object to add on the map
   */
  const addBeeToMap = function (bee) {
    if (bee.latitude !== 0 && bee.longitude !== 0) {
      L.marker([bee.latitude, bee.longitude])
        .bindPopup(`<b>${bee.name}</b>`)
        .addTo(hive);
    }
  };

  $.get('/api/me')
    .fail(function (ex) {
      const status = ex.status;
      if (status === 401 || status === 403) {
        $('.ui.modal#login')
          .modal({
            blurring: true,
            closable: false
          })
          .modal('show')
      }
    })
    .done(function () {
      $.get('/api/hive')
        .done(function (bees) {
          bees.forEach(addBeeToMap)
        });
    });

  const editProfileModal = $('.ui.modal#profile')
    .modal({
      blurring: true,
      onApprove: function () {
        return false;
      },
      onDeny: function () {
        profileForm.form('reset');
      },
      onShow: function () {
        profileForm.addClass('loading');
        $.get('/api/me')
          .done(function (bee) {
              const {name, email, latitude, longitude} = bee;
              profileForm.form('set values', {
                name, email, latitude, longitude
              });
            }
          )
          .fail(function () {
            profileForm.form('set error');
            profileForm.transition('shake');
          })
          .always(function () {
            profileForm.toggleClass('loading');
          });
      }
    })
    .modal('attach events', '#profileBtn', 'show');

  /**
   * Define the acceptable boundaries of a number input.
   *
   * Syntax is minValue:maxValue.
   */
  $.fn.form.settings.rules.boundaries = function (value, boundaries) {
    const [minValue, maxValue] = boundaries.split(':');
    return Number(value) >= Number(minValue) && Number(value) <= Number(maxValue);
  };

  const profileForm = editProfileModal.find('.ui.form');
  profileForm.form({
    inline: true,
    onSuccess: function (e, fields) {
      if (e !== undefined) e.preventDefault();
      profileForm.addClass('loading');

      $.ajax({
        type: 'POST',
        url: '/api/bee',
        data: JSON.stringify(fields),
        contentType: 'application/json',
        beforeSend: function (req) {
          req.setRequestHeader('X-XSRF-TOKEN', getCookie('XSRF-TOKEN'))
        }
      })
        .done(function (bee) {
          profileForm.form('reset');
          editProfileModal.modal('hide');
          addBeeToMap(bee);
          hive.panTo([fields.latitude, fields.longitude]);
        })
        .fail(function () {
          profileForm.form('set error');
          profileForm.transition('shake');
        })
        .always(function () {
          profileForm.removeClass('loading');
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
  $('button#validate-form').click(function (event) {
    event.preventDefault();
    profileForm.form('validate form');
  });

  $('button#logoutBtn')
    .click(function (event) {
      event.preventDefault();
      $.ajax({
        type: 'POST',
        url: '/api/me/logout',
        beforeSend: function(req) {
          req.setRequestHeader('X-XSRF-TOKEN', getCookie('XSRF-TOKEN'))
        }
      })
        .done(function() {
          window.location.reload();
        });
    });

  const locationButton = $('.ui.button#location');
  if (!("geolocation" in navigator)) {
    locationButton.toggleClass('disabled');
  }
  locationButton.click(function (event) {
    event.preventDefault();
    locationButton.toggleClass('loading');
    navigator.geolocation.getCurrentPosition(function (position) {
      profileForm.form('set values', {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
      });
      locationButton.toggleClass('loading');
    }, function () {
      locationButton.toggleClass('loading');
      locationButton.toggleClass('disabled');
    })
  });

  /**
   * Function to get access to a specific cookie value.
   *
   * @param name the name of the cookie you want to access.
   * @return the value of the cookie, or ''
   */
  const getCookie = function (name) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      let [key, value] = cookies[i].split('=');
      if (key.trim() === name) return value.trim();
    }
    return '';
  };
})();
