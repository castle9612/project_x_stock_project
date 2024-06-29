import '../css/membership.css';

function Membership() {

  return (
    <div id="App">
        <div id="header"/>
        <div id="body">
            <form id="MembershipForm">
                <h2>회원가입</h2>
                <input type="text" name="userID" placeholder="아이디"/><hr/>
                <input type="text" name="userPW" placeholder="비밀번호"/><hr/>
                <input type="text" name="userEmail" placeholder="이메일"/><hr/>
                <button type="submit" id="membership_btn">
                    <span>회원가입</span>
                </button>
            </form>
        </div>
        <div id="footer"/>
    </div>
  );
}

export default Membership;