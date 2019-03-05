
function (request, state, logger) {
    logger.info('origin called');
    state.requests = state.requests || 0;
    state.requests += 1;
    return {
        headers: {
        'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "as": "AS265252 D1 Telecomunicacoes Ltda",
            "city": "Viamão",
            "country": "Brazil",
            "countryCode": "BR",
            "isp": "D1 Telecomunicacoes Ltda",
            "lat": -30.0833,
            "lon": -51.0333,
            "org": "D1 Telecomunicacoes Ltda",
            "query": "168.0.148.144",
            "region": "RS",
            "regionName": "Rio Grande do Sul",
            "status": "success",
            "timezone": "America/Sao_Paulo",
            "zip": "91435"
            })
    };
}