import React from 'react'
import {
  Button,
  Container,
  Form,
  Icon,
  Input,
  Menu,
  Modal,
} from 'semantic-ui-react'

export default class CreateBee extends React.Component {
  render() {
    return (
      <Modal size='tiny' dimmer='inverted' trigger={<Menu.Item><Icon name='plus' /></Menu.Item>}>
        <Modal.Header>Create new Bee</Modal.Header>
        <Modal.Content>
          <Form>
            <Form.Field required>
              <label>Name</label>
              <input placeholder='Name' name='name' />
            </Form.Field>
            <Form.Field required>
              <label>Email</label>
              <Input icon='at' iconPosition='left' type='email' placeholder='Email' name='email' />
            </Form.Field>
            <Form.Field required>
              <label>Location</label>
              <input type='hidden' name='latitude' />
              <input type='hidden' name='longitude' />
              <Input icon='search' iconPosition='left' type='email' placeholder='Location' name='location' />
            </Form.Field>
          </Form>
        </Modal.Content>
        <Modal.Actions>
          <Button.Group>
            <Button color='red' content='Cancel' negative />
            <Button.Or />
            <Button color='green' content='Ok' positive />
          </Button.Group>
        </Modal.Actions>
      </Modal>
    )
  }
}
