import request from './common/Request';

export default class Count {
    static count(type, current, size) {
        return request({
            url: '/count',
            method: 'get',
            params: {type, current: current, size: size},
        })
    }
    static count2(type) {
        return request({
            url: '/count2',
            method: 'get',
            params: {type},
        })
    }
}