import React from 'react'
import L from 'leaflet'

export default class Hive extends React.Component {
  constructor() {
    super()
  }

  componentDidMount() {
    const map = L.map('hive').setView([48.85, 2.35], 3);
    const tileLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 7,
      minZoom: 3,
      attribution: '&copy;&nspar;<a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(map);
  }

  render() {
    return (
      <div id="hive" />
    )
  }
}
