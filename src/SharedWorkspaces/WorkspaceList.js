import React from "react";
import {connect} from "react-redux";
import {push} from "react-router-redux";
import Spinner from "../Common/Spinner";
import WorkspaceListItem from "./WorkspaceListItem";
import "./WorkspaceList.css";

const WorkspaceList = ({push, loading, items}) => {
    let content;
    if (loading) {
        content = <Spinner>Loading workspaces...</Spinner>;
    } else if (!items || (items.length && items.length <= 0)) {
        content = <div className="WorkspaceList-nothing">No projects created yet.</div>;
    } else {
        content = (
            <div className="WorkspaceList">
                {items.map(item => {
                    return <WorkspaceListItem key={item.id}
                                              workspace={item}
                                              push={push} />;
                })}
            </div>
        );
    }
    return content;
};

export default connect(
    state => state.sharedWorkspaces,
    {push}
)(WorkspaceList);