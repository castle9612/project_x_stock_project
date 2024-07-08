import React, { useState } from 'react';
import axios from 'axios';
import '../css/start.css';

function Start() {
    const [userID, setUserID] = useState('');
    const [userPW, setUserPW] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/login', {
                userID,
                userPW
            });

            if (response.data.success) {
                window.location.href = '/main';
            } else {
                setError('로그인 실패: ' + response.data.message);
            }
        } catch (error) {
            alert('로그인 중 오류가 발생했습니다.');
        }
    }

    return (
        <div id="App">
            <div id="header"/>
            <div id="body">
                <form id="Login" onSubmit={handleLogin}>
                    <h2>로그인</h2>
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
                    <a href="/membership">회원가입</a><hr/>
                    <button type="submit" id="Login_btn">
                        <span>로그인</span>
                    </button>
                    {error && <p className="error">{error}</p>}
                </form>
            </div>
            <div id="footer"/>
        </div>
    );
}

export default Start;