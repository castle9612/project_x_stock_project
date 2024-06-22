import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import App1 from './components/start.js';
import App2 from './components/login.js';
import App3 from './components/main.js';
import App4 from './components/membership.js';
import { Component } from 'react';

class App extends Component{
  constructor(props){
    super(props)
    this.state={
      
    }
  }

  render(){
    return(
      <div id='App'>
        <BrowserRouter>
            <Routes>
              <Route path='/' element={<App1/>}/>
              <Route path='/login' element={<App2/>}/>
              <Route path='/main' element={<App3/>}/>
              <Route path='/membership' element={<App4/>}/>
            </Routes>   
        </BrowserRouter> 
      </div>
    )
  }
}

export default App;
