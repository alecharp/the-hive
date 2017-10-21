import React from 'react'
import ReactDOM from 'react-dom'

import L from 'leaflet'

// TODO define our own images for bees and hives
L.Icon.Default.imagePath = '/';
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

import 'ui'

import Hive from 'components/Hive'

ReactDOM.render(
  <Hive />,
  document.getElementById('content')
)
