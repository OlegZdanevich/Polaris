import React, { Component } from 'react';
import './Project.css';
import { Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';
class Project extends Component {
    render() {
        return (
            <div className="poll-content">
                <div className="poll-header">
                    <div className="poll-creator-info">
                        <Link className="creator-link" to={`/users/${this.props.project.createdBy.username}`}>
                            <Avatar className="poll-creator-avatar" 
                             style={{ backgroundColor: getAvatarColor(this.props.project.createdBy.name)}} >
                                {this.props.project.createdBy.name[0].toUpperCase()}
                            </Avatar>
                            <span className="poll-creator-name">
                                {this.props.project.createdBy.name}
                            </span>
                            <span className="poll-creator-username">
                                @{this.props.project.createdBy.username}
                            </span>
                            <span className="poll-creation-date">
                                {formatDateTime(this.props.project.creationDateTime)}
                            </span>
                        </Link>
                    </div>
                    <div className="poll-question">
                        {this.props.project.project}
                    </div>
                    <Link to={'/project/'+ this.props.project.id}>Открыть проект</Link>
                </div>
            </div>
        );
    }
}


export default Project;