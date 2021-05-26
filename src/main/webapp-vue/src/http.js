import axios from 'axios';

export const HTTP = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? '/': 'http://localhost:8080/',
})