import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import App1 from './components/start.js';
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
            </Routes>   
        </BrowserRouter> 
      </div>
    )
  }
}

export default App;
