import React, { Component } from 'react';
import './ServerError.css';
import { Link } from 'react-router-dom';
import { Button } from 'antd';

class ServerError extends Component {
    render() {
        return (
            <div className="server-error-page">
                <h1 className="server-error-title">
                    500
                </h1>
                <div className="server-error-desc">
                    Ой! Что-то пошло не так на нашем сервере. Почему бы тебе не вернуться назад?
                </div>
                <Link to="/"><Button className="server-error-go-back-btn" type="primary" size="large">Назад</Button></Link>
            </div>
        );
    }
}

export default ServerError;