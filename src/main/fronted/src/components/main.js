import '../css/main.css';

function Main() {
    const GoStart = () => {
        window.location.href = '/';
    }

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
                주가그래프
            </div>
            <div id="contents">
                보유금액, 충전, 매수,매매 등등
            </div>
        </div>
    </div>
  );
}

export default Main;