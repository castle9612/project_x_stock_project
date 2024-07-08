import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Chart, ChartCanvas, CandlestickSeries, XAxis, YAxis, CrossHairCursor, MouseCoordinateX,
    MouseCoordinateY, discontinuousTimeScaleProviderBuilder } from 'react-financial-charts';
import { timeFormat } from 'd3-time-format';
import * as d3 from 'd3-format';
import '../css/main.css';

function Main() {
    const [chartData, setChartData] = useState([]);

    const fetchData = async () => {
        try {
            const response = await axios.get('/stock-data-for-graph', {
                params: {
                    stockCode: '005930',
                    startDate: '2024-05-05',
                    endDate: '2024-06-20'
                }
            });
            const parsedData = response.data.map(item => ({
                ...item,
                date: new Date(item.date)
            }));
            setChartData(parsedData);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const GoStart = () => {
        window.location.href = '/';
    };

    const stockInfo = {
        "stockCode": "005930",
        "stockList": "KOSPI",
        "company": "삼성전자",
        "stockType": "보통주",
        "marketCapitalization": 450000000,
        "dividend": 2.5,
        "currentPrice": 71000,
        "volume": 10000000,
        "eps": 5000,
        "per": 14.2,
        "pbr": 1.5,
        "roe": 15.8,
        "sector": "전자제품 제조업",
        "listedDate": "1975-06-11",
        "description": "삼성전자는 한국의 대표적인 전자제품 제조 기업으로, 스마트폰, TV, 반도체 등을 생산합니다.",
        "lastUpdated": "2024-07-07T15:30:00"
    };

    const xScaleProvider = discontinuousTimeScaleProviderBuilder().inputDateAccessor(d => d.date);
    const { data: chartDataParsed, xScale, xAccessor, displayXAccessor } = xScaleProvider(chartData);

    const xExtents = [xAccessor(chartDataParsed[0]), xAccessor(chartDataParsed[chartDataParsed.length - 1])];

    return (
        <div id="App">
            <div id="header_main">
                <button id="logout_btn" onClick={GoStart}>
                    <span>로그아웃</span>
                </button>
            </div>
            <div id="body_main">
                <div id="stock_info">
                    <h3>주가 정보</h3>
                    <div id="sample1">
                        <p><strong>종목명:</strong> {stockInfo.company}</p>
                        <p><strong>종목코드:</strong> {stockInfo.stockCode}</p>
                        <p><strong>시가총액:</strong> {stockInfo.marketCapitalization}</p>
                        <p><strong>현재가:</strong> {stockInfo.currentPrice}</p>
                    </div>
                </div>
                <div id="chart">
                    <div id="blank"/>
                    <ChartCanvas
                        height={500}
                        width={1100}
                        ratio={3}
                        margin={{ left: 50, right: 50, top: 10, bottom: 30 }}
                        data={chartDataParsed}
                        seriesName="MSFT"
                        xScale={xScale}
                        xAccessor={xAccessor}
                        displayXAccessor={displayXAccessor}
                        xExtents={xExtents}
                        style={{ color: 'white' }}
                    >
                        <Chart id={1} yExtents={d => [d.high, d.low]}>
                            <XAxis axisAt="bottom" orient="bottom" />
                            <YAxis axisAt="left" orient="left" ticks={5} />
                            <MouseCoordinateX
                                at="bottom"
                                orient="bottom"
                                displayFormat={timeFormat("%Y-%m-%d")}
                            />
                            <MouseCoordinateY
                                at="left"
                                orient="left"
                                displayFormat={d3.format(".2f")}
                            />
                            <CandlestickSeries />
                            <CrossHairCursor />
                        </Chart>
                    </ChartCanvas>
                </div>
                <div id="contents">
                    <div id="userinfo">
                        <p><strong>아이디 : </strong></p>
                        <p>보유금액</p>
                    </div>
                    <div id="buy_stock">
                        <h3>주식 구매</h3>
                        <p>userid</p>
                        <input type="text" name="userid"/>
                        <p>stockId</p>
                        <input type="text" name="stockId"/>
                        <p>money</p>
                        <input type="text" name="money"/><hr/>
                        <button type="submit" id="buy_stock_btn">
                            <span>구매</span>
                        </button>
                    </div>
                    <div id="sell_stock">
                        <h3>주식 판매</h3>
                        <p>userid</p>
                        <input type="text" name="userid"/>
                        <p>stockId</p>
                        <input type="text" name="stockId"/>
                        <p>quantity</p>
                        <input type="text" name="quantity"/><hr/>
                        <button type="submit" id="sell_stock_btn">
                            <span>판매</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Main;