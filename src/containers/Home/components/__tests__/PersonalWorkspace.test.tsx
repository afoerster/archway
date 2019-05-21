import * as React from 'react';
import { shallow } from 'enzyme';

import PersonalWorkspace from '../PersonalWorkspace';
import { Workspace } from '../../../../models/Workspace';

describe('PersonalWorkspace', () => {
  it('renders correctly', () => {
    const workspace: Workspace = {
      id: 0,
      name: '',
      summary: '',
      description: '',
      behavior: '',
      requested_date: new Date(0),
      requester: '',
      single_user: false,
      compliance: {
        phi_data: false,
        pci_data: false,
        pii_data: false,
      },
      data: [{
        id: 1,
        name: '',
        location: '',
        size_in_gb: 0,
        consumed_in_gb: 0,
        managing_group: {
          group: {
            common_name: '',
            distinguished_name: '',
            sentry_role: '',
            attributes: [['','']]
          },
        },
      }],
      processing: [{
        id: 0,
        pool_name: '',
        max_cores: 0,
        max_memory_in_gb: 0,
      }],
      topics: [],
      applications: [],
    };
    const services = {
      yarn: {
        //
      },
      hue: {
        //
      },
      hive: {
        thrift: [
          { host: '', port: '' },
        ],
      },
      impala: {
        //
      },
    };
    const wrapper = shallow(
      <PersonalWorkspace
        workspace={workspace}
        services={services}
        loading={false}
        requestWorkspace={() => null}
      />,
    );
    expect(wrapper).toMatchSnapshot();
  });
});
