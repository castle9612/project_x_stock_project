import React from 'react';
import { Chart, ChartCanvas, CandlestickSeries, XAxis, YAxis, CrossHairCursor, MouseCoordinateX,
    MouseCoordinateY, discontinuousTimeScaleProviderBuilder } from 'react-financial-charts';
import { timeFormat } from 'd3-time-format';
import * as d3 from 'd3-format';
import '../css/main.css';

function Main() {
    const GoStart = () => {
        window.location.href = '/';
    };

    const data = [
        {
            "date": "2024-06-20",
            "open": 81500,
            "high": 82200,
            "low": 81200,
            "close": 81600
        },
        {
            "date": "2024-06-19",
            "open": 81100,
            "high": 82500,
            "low": 80500,
            "close": 81200
        },
        {
            "date": "2024-06-18",
            "open": 78700,
            "high": 80200,
            "low": 78600,
            "close": 79800
        },
        {
            "date": "2024-06-17",
            "open": 79200,
            "high": 79500,
            "low": 78000,
            "close": 78100
        },
        {
            "date": "2024-06-14",
            "open": 79700,
            "high": 80500,
            "low": 79000,
            "close": 79600
        },
        {
            "date": "2024-06-13",
            "open": 78400,
            "high": 79000,
            "low": 77800,
            "close": 78600
        },
        {
            "date": "2024-06-12",
            "open": 75300,
            "high": 77100,
            "low": 75200,
            "close": 76500
        },
        {
            "date": "2024-06-11",
            "open": 75900,
            "high": 76000,
            "low": 75100,
            "close": 75200
        },
        {
            "date": "2024-06-10",
            "open": 76100,
            "high": 76600,
            "low": 75600,
            "close": 75700
        },
        {
            "date": "2024-06-07",
            "open": 78400,
            "high": 78600,
            "low": 77100,
            "close": 77300
        },
        {
            "date": "2024-06-05",
            "open": 78000,
            "high": 78000,
            "low": 76800,
            "close": 77400
        },
        {
            "date": "2024-06-04",
            "open": 74900,
            "high": 76100,
            "low": 74900,
            "close": 75300
        },
        {
            "date": "2024-06-03",
            "open": 74400,
            "high": 76400,
            "low": 74200,
            "close": 75700
        },
        {
            "date": "2024-05-31",
            "open": 74500,
            "high": 74700,
            "low": 73500,
            "close": 73500
        },
        {
            "date": "2024-05-30",
            "open": 74800,
            "high": 75200,
            "low": 73500,
            "close": 73500
        },
        {
            "date": "2024-05-29",
            "open": 77700,
            "high": 78200,
            "low": 75200,
            "close": 75200
        },
        {
            "date": "2024-05-28",
            "open": 76500,
            "high": 78000,
            "low": 76200,
            "close": 77600
        },
        {
            "date": "2024-05-27",
            "open": 75300,
            "high": 78200,
            "low": 74000,
            "close": 77200
        },
        {
            "date": "2024-05-24",
            "open": 76800,
            "high": 77000,
            "low": 75700,
            "close": 75900
        },
        {
            "date": "2024-05-23",
            "open": 77700,
            "high": 79100,
            "low": 77100,
            "close": 78300
        },
        {
            "date": "2024-05-22",
            "open": 78100,
            "high": 78700,
            "low": 77300,
            "close": 77700
        },
        {
            "date": "2024-05-21",
            "open": 78500,
            "high": 79000,
            "low": 78200,
            "close": 78400
        },
        {
            "date": "2024-05-20",
            "open": 78100,
            "high": 79100,
            "low": 77900,
            "close": 78900
        },
        {
            "date": "2024-05-17",
            "open": 78600,
            "high": 78800,
            "low": 77200,
            "close": 77400
        },
        {
            "date": "2024-05-16",
            "open": 80200,
            "high": 80300,
            "low": 78100,
            "close": 78200
        },
        {
            "date": "2024-05-14",
            "open": 78600,
            "high": 78800,
            "low": 77900,
            "close": 78300
        },
        {
            "date": "2024-05-13",
            "open": 79400,
            "high": 79900,
            "low": 77600,
            "close": 78400
        },
        {
            "date": "2024-05-10",
            "open": 80400,
            "high": 81100,
            "low": 78900,
            "close": 79200
        },
        {
            "date": "2024-05-09",
            "open": 81100,
            "high": 81500,
            "low": 79700,
            "close": 79700
        },
        {
            "date": "2024-05-08",
            "open": 80800,
            "high": 81400,
            "low": 80500,
            "close": 81300
        },
        {
            "date": "2024-05-07",
            "open": 79600,
            "high": 81300,
            "low": 79400,
            "close": 81300
        }
    ]

    const parsedData = data.map(item => ({
        ...item,
        date: new Date(item.date)
    }));

    const xScaleProvider = discontinuousTimeScaleProviderBuilder().inputDateAccessor(d => d.date);
    const { data: chartData, xScale, xAccessor, displayXAccessor } = xScaleProvider(parsedData);

    const xExtents = [xAccessor(chartData[0]), xAccessor(chartData[chartData.length - 1])];


    return (
        <div id="App">
            <div id="header_main">
                <button id="logout_btn" onClick={GoStart}>
                    <span>로그아웃</span>
                </button>
            </div>
            <div id="body_main">
                <div id="stock_info">
                    주가정보
                </div>
                <div id="chart">
                    <div id="blank"/>
                    <ChartCanvas
                        height={500}
                        width={1100}
                        ratio={3}
                        margin={{ left: 50, right: 50, top: 10, bottom: 30 }}
                        data={chartData}
                        seriesName="MSFT"
                        xScale={xScale}
                        xAccessor={xAccessor}
                        displayXAccessor={displayXAccessor}
                        xExtents={xExtents}
                        style={{ Color: 'white' }}
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
                    보유금액, 충전, 매수, 매매 등등
                </div>
            </div>
        </div>
    );
}

export default Main;