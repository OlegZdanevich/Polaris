import React, {Component} from 'react';
import {createProject} from '../util/APIUtils';
import {PROJECT_NAME_MAX_LENGTH} from '../constants';
import './NewProject.css';
import {Form, Input, Button, notification} from 'antd';

const FormItem = Form.Item;
const {TextArea} = Input

class NewProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            project: {
                text: ''
            },
            choices: [{
                text: ''
            }, {
                text: ''
            }],
            pollLength: {
                days: 1,
                hours: 0
            }
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleProjectChange = this.handleProjectChange.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }


    handleSubmit(event) {
        event.preventDefault();
        const pollData = {
            project: this.state.project.text
        };

        createProject(pollData)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'Вы вышли из сессии. Пожалуйста зайдите в приложение еще раз.');
            } else {
                notification.error({
                    message: 'Polaris App',
                    description: error.message || 'Извините! Что-то пошло не так. Попробуйте еще раз!'
                });
            }
        });
    }

    validateQuestion = (questionText) => {
        if (questionText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Пожалуйста введите имя вашего проекта!'
            }
        } else if (questionText.length > PROJECT_NAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Имя проекта слишком длинное (Максимально разрешено ${PROJECT_NAME_MAX_LENGTH} символов)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    handleProjectChange(event) {
        const value = event.target.value;
        this.setState({
            project: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }


    handleChoiceChange(event, index) {
        const choices = this.state.choices.slice();
        const value = event.target.value;

        choices[index] = {
            text: value,
            ...this.validateChoice(value)
        }

        this.setState({
            choices: choices
        });
    }


    handlePollDaysChange(value) {
        const pollLength = Object.assign(this.state.pollLength, {days: value});
        this.setState({
            pollLength: pollLength
        });
    }

    handlePollHoursChange(value) {
        const pollLength = Object.assign(this.state.pollLength, {hours: value});
        this.setState({
            pollLength: pollLength
        });
    }

    isFormInvalid() {
        if (this.state.project.validateStatus !== 'success') {
            return true;
        }
    }

    render() {
        return (
            <div className="new-project-container">
                <h1 className="page-title">Создать проект</h1>
                <div className="new-poll-content">
                    <Form onSubmit={this.handleSubmit} className="create-poll-form">
                        <FormItem validateStatus={this.state.project.validateStatus}
                                  help={this.state.project.errorMsg} className="project-form-row">
                            <input size="68" placeholder="Введите имя проекта" type="text" name="project" value={this.state.project.text}  style={{fontSize: '16px'}} onChange={this.handleProjectChange}/>
                        </FormItem>
                        <FormItem className="project-form-row">
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    disabled={this.isFormInvalid()}
                                    className="create-project-form-button">Создать проект</Button>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }
}


export default NewProject;