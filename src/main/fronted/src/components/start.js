import '../css/start.css';

function Start() {
    const GoMain = () => {
        window.location.href = '/main';
    }

  return (
    <div id="App">
        <div id="header"/>
        <div id="body">
            <form id="Login">
                <h2>로그인</h2>
                <input type="text" name="userID" placeholder="아이디"/><hr/>
                <input type="text" name="userPW" placeholder="비밀번호"/><hr/>
                <a href="/membership">회원가입</a><hr/>
                <button type="submit" id="Login_btn" onClick={GoMain}>
                    <span>로그인</span>
                </button>
            </form>
        </div>
        <div id="footer"/>
    </div>
  );
}

export default Start;