import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import Start from './components/start.js';
import Main from './components/main.js';
import Membership from './components/membership.js';
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
              <Route path='/' element={<Start/>}/>
              <Route path='/main' element={<Main/>}/>
              <Route path='/membership' element={<Membership/>}/>
            </Routes>   
        </BrowserRouter> 
      </div>
    )
  }
}

export default App;
