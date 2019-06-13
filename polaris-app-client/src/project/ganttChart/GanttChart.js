import React, {Component} from 'react';
import {GanttChart} from '@hh.ru/react-d3-chart-graphs';
import {timeFormat} from 'd3-time-format';
import 'bootstrap/dist/css/bootstrap.min.css'
import LoadingIndicator from "../../common/LoadingIndicator";
import {countProject, countProjectBest} from "../../util/APIUtils"


const axesProps = {
    legend: {
        xAxis: '',
        yAxis: '',
    },
    padding: {
        xAxis: 5,
        yAxis: 5,
    },
    tickFormat: {
        xAxis: timeFormat('%d/%m'),
    },
};

class Chart extends Component {
    constructor(props) {
        super(props);
        this.state = {
            project: this.props.project,
            projectData: this.props.projectData,
            stackColors: {},
            dataToDraw: [],
            isLoading: false,
            countByTime: false,
            selectValue: '---'
        };
        this.getStackColors = this.getStackColors.bind(this);
        this.getDataToDrawStart = this.getDataToDrawStart.bind(this);
        this.getDataToDraw = this.getDataToDraw.bind(this);
        this.count = this.count.bind(this);
    }

    static getRandomColor() {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    static getDateToStart(plus, isStart) {
        const today = new Date();
        if (isStart) {
            return Chart.addDays(today, plus).toGMTString();
        } else {
            return Chart.addHours(Chart.addDays(today, plus), 1).toGMTString();
        }
    }

    static getDate(plus) {
        const today = new Date();
        return Chart.addDays(today, plus).toGMTString();
    }


    static addDays(date, days) {
        date.setDate(date.getDate() + days);
        return date;
    }

    static addHours(date, hours) {
        date.setHours(date.getHours() + hours);
        return date;
    }


    getStackColors() {
        const colors = {};
        const data = this.state.projectData;
        for (let i = 0; i < data.flows.length; i++) {
            colors[data.flows[i].flowId] = {
                color: Chart.getRandomColor(),
                legend: "Поток: " + (data.flows[i].flowId + 1)
            };
        }
        this.setState(() => {
                return {
                    stackColors: colors
                }
            }
        )
    }

    getDataToDrawStart() {
        const data = [];
        const projectData = this.state.projectData.flows;
        for (let i = 0; i < projectData.length; i++) {
            const allocations = projectData[i].allocations;
            for (let k = 0; k < allocations.length; k++) {
                data.push({
                        titleBar: "Работа: " + (allocations[k].jobId + 1),
                        values: [
                            {
                                title: projectData[i].flowId,
                                dateStart: Chart.getDateToStart(allocations[k].start, true),
                                dateEnd: Chart.getDateToStart(allocations[k].end, false),
                            }
                        ]
                    }
                )
            }

        }
        this.setState(() => {
                return {
                    dataToDraw: data
                }
            }
        )

    }

    getDataToDraw() {
        const data = [];
        const projectData = this.state.projectData.flows;
        for (let i = 0; i < projectData.length; i++) {
            const allocations = projectData[i].allocations;
            for (let k = 0; k < allocations.length; k++) {
                data.push({
                        titleBar: "Работа: " + (allocations[k].jobId + 1),
                        values: [
                            {
                                title: projectData[i].flowId,
                                dateStart: Chart.getDate(allocations[k].start),
                                dateEnd: Chart.getDate(allocations[k].end),
                            }
                        ]
                    }
                )
            }

        }
        console.log(data);
        this.setState(() => {
                return {
                    dataToDraw: data
                }
            }
        )

    }

    componentWillMount() {
        this.getStackColors();
        this.getDataToDrawStart();
    }

    count() {
        this.setState({isLoading: true});
        let promise = this.state.selectValue === '---' ? countProject(this.state.project) : countProjectBest(this.state.project, this.state.selectValue);
        promise.then(response => {
            this.setState(() => {
                return {projectData: response}
            });
            this.getDataToDraw();
            this.setState({
                isLoading: false
            })
        });
    }

    _handleChange = (event) => {
        console.log(event.target.value);
        this.setState({selectValue: event.target.value})
    };


    render() {
        return (
            <div>
                {this.state.isLoading ?
                    <LoadingIndicator/> :
                    <div>
                        <button type="button" className="btn btn-outline-success"
                                style={{marginLeft: "50%", marginTop: "5px"}}
                                onClick={this.count}>Посчитать
                        </button>
                        <div className="row" style={{marginLeft: "90%", marginTop: "5px"}}>
                            <div className="col-xs-3">
                                Минуты:
                                <select className="form-control" id="sel1" onChange={this._handleChange}>
                                    <option>---</option>
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                </select>
                            </div>
                        </div>

                        <GanttChart
                            axesProps={axesProps}
                            data={this.state.dataToDraw}
                            paddingMultiplier={0.5}
                            stackColors={this.state.stackColors}/>

                    </div>
                }
            </div>

        );
    }
}

export default Chart