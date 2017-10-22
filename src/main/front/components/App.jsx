import React from 'react'
import {
  Container,
  Menu,
} from 'semantic-ui-react'

import CreateBee from './CreateBee'
import Hive from './Hive'

export default class App extends React.Component {
  constructor() {
    super()
  }

  render() {
    return (
      <div>
        <Menu icon vertical inverted id='app-menu'>
          <CreateBee />
        </Menu>

        <Container fluid>
          <Hive />
        </Container>
      </div>
    )
  }
}
