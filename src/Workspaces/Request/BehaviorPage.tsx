import * as React from 'react';
import {Tooltip, Row, Icon, Col} from 'antd';
import Behavior from '../../Common/Behavior';

interface Props {
  selected: string
  onChange: (behavior: string) => void
}

const BehaviorPage = ({ selected, onChange }: Props) => (
  <div>
    <h3 style={{ display: 'inline-block', marginRight: 7 }}>
      What kind of behavior should we manage?
    </h3>
    <Tooltip title="Heimdali will set up a structure that enables your team to work on a workspace in a certain way. See descriptions below for more information">
      <Icon theme="twoTone" type="question-circle" />
    </Tooltip>
    <Row type="flex" justify="center">
      <Col span={6}>
        <Behavior
          behaviorKey="simple"
          selected={selected === "simple"}
          onChange={onChange}
          icon="team"
          title="Simple"
          description="A simple place for multiple users to collaborate on a solution."
          useCases={["brainstorming", "evaluation", "prototypes"]} />
      </Col>
      <Col span={6}>
        <Behavior
          behaviorKey="structured"
          selected={selected === "structured"}
          onChange={onChange}
          icon="deployment-unit"
          title="Structured"
          description={`Data moves through three steps: raw, staging, modeled. Each step represents a more "structured" version of the data.`}
          useCases={["publishings", "data assets", "external interfacing"]} />
      </Col>
    </Row>
  </div>
);

export default BehaviorPage;