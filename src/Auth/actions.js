export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';

export function login({username, password}) {
    return {
        type: LOGIN_REQUEST,
        username,
        password
    };
}

export function loginSuccess(token) {
    return {
        type: LOGIN_SUCCESS,
        token
    };
}

export function loginError(error) {
    return {
        type: LOGIN_FAILURE,
        error
    };
}

export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';



export const TOKEN_EXTRACTED = 'TOKEN_EXTRACTED';
export const TOKEN_NOT_AVAILABLE = 'TOKEN_NOT_AVAILABLE';

export function tokenExtracted(token) {
    return {
        type: TOKEN_EXTRACTED,
        token
    };
}

export function tokenNotAvailalbe() {
    return {
        type: TOKEN_NOT_AVAILABLE
    };
}

export const PROFILE_READY = 'PROFILE_READY';

export function profileReady(profile) {
    return {
        type: PROFILE_READY,
        profile
    };
}