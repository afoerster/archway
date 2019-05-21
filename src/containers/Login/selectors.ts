import { createSelector } from 'reselect';
import { authSelector } from '../../redux/selectors';

export const isLoggingIn = () => createSelector(
  authSelector,
  (authState) => authState.get('loading'),
);

export const loginError = () => createSelector(
  authSelector,
  (authState) => authState.get('error'),
);
