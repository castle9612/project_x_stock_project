import React, { useState } from 'react';
import axios from 'axios';
import '../css/membership.css';

function Membership() {
    const [userID, setUserID] = useState('');
    const [userPW, setUserPW] = useState('');
    const [userEmail, setUserEmail] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://34.22.73.38:8080/api/users/register', {
                userID,
                userPW,
                userEmail
            });

            if (response.data.success) {
                setSuccess('회원가입이 성공적으로 완료되었습니다. 로그인 페이지로 이동합니다.');
                setTimeout(() => {
                    window.location.href = '/';
                }, 2000); // Redirect after 2 seconds
            } else {
                setError('회원가입 실패: ' + response.data.message);
            }
        } catch (error) {
            setError('회원가입 중 오류가 발생했습니다.');
        }
    }

    return (
        <div id="App">
            <div id="header"/>
            <div id="body">
                <form id="MembershipForm" onSubmit={handleRegister}>
                    <h2>회원가입</h2>
                    <input
                        type="text"
                        name="userID"
                        placeholder="아이디"
                        value={userID}
                        onChange={(e) => setUserID(e.target.value)}
                    /><hr/>
                    <input
                        type="password"
                        name="userPW"
                        placeholder="비밀번호"
                        value={userPW}
                        onChange={(e) => setUserPW(e.target.value)}
                    /><hr/>
                    <input
                        type="email"
                        name="userEmail"
                        placeholder="이메일"
                        value={userEmail}
                        onChange={(e) => setUserEmail(e.target.value)}
                    /><hr/>
                    <button type="submit" id="membership_btn">
                        <span>회원가입</span>
                    </button>
                    {error && <p className="error">{error}</p>}
                    {success && <p className="success">{success}</p>}
                </form>
            </div>
            <div id="footer"/>
        </div>
    );
}

export default Membership;