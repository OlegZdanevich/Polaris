import React, {Component} from 'react';
import {getProject} from '../util/APIUtils'
import Chart from './ganttChart/GanttChart'
import LoadingIndicator from "../common/LoadingIndicator";

class ProjectManagement extends Component {
    constructor(props) {
        super(props);
        this.state = {
            project: this.props.match.params.projectname,
            projectData: {},
            isLoading: true
        };
    }

    componentDidMount() {
        let promise = getProject(this.state.project);
        this.setState({
            isLoading: true
        });
        promise.then(response => {
            this.setState({
                projectData: response,
                isLoading: false
            });
            console.log(response);
        });

    }


    render() {
        const elem=this.state.isLoading ?
            <LoadingIndicator/> : <Chart project={this.state.project} projectData={this.state.projectData}/>;
        return (
            <div>
                {elem}
            </div>
        );
    }
}

export default ProjectManagement