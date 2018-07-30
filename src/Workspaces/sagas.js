import { all, fork } from 'redux-saga/effects';

import members from './Members/sagas';
import request from './Request/sagas';
import status from './Status/sagas';
import details from './WorkspaceDetails/sagas';
import listing from './WorkspaceList/sagas';

export default function* root() {
  yield all([
    fork(members),
    fork(request),
    fork(status),
    fork(details),
    fork(listing),
  ]);
}
