import React, {Component} from 'react';
import {getUserCreatedProject} from '../util/APIUtils';
import Project from './Project';
import LoadingIndicator from '../common/LoadingIndicator';
import {Button, Icon} from 'antd';
import {PROJECT_LIST_SIZE} from '../constants';
import {withRouter} from 'react-router-dom';
import './ProjectList.css';

class ProjectList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            project: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
        this.loadProjectList = this.loadProjectList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadProjectList(page = 0, size = PROJECT_LIST_SIZE) {
        let promise;
        if (this.props.currentUser) {
            console.log(this.props.currentUser);
            promise = getUserCreatedProject(this.props.currentUser.username, page, size);
        }

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                console.log(response);
                const polls = this.state.project.slice();

                this.setState({
                    project: polls.concat(response.content),
                    page: response.page,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });

    }

    componentDidMount() {
        this.loadProjectList();
    }

    componentDidUpdate(nextProps) {
        if (this.props.isAuthenticated !== nextProps.isAuthenticated) {
            this.setState({
                project: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                isLoading: false
            });
            this.loadProjectList();
        }
    }

    handleLoadMore() {
        this.loadProjectList(this.state.page + 1);
    }


    render() {
        const projectViews = [];
        console.log(this.state.project);
        this.state.project.forEach((project) => {
            projectViews.push(<Project
                key={project.id}
                project={project}/>)
        });

        return (
            <div className="polls-container">
                {projectViews}
                {
                    !this.state.isLoading && this.state.project.length === 0 ? (
                        <div className="no-polls-found">
                            <span>Проектов не найдено.</span>
                        </div>
                    ) : null
                }
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-polls">
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus"/> Загрузить еще
                            </Button>
                        </div>) : null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator/> : null
                }
            </div>
        );
    }
}

export default withRouter(ProjectList);