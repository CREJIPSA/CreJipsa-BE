import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 1,
  iterations: 1,
};

const BASE_URL = 'http://localhost:8080';
const TOKEN = __ENV.TOKEN;

export default function () {
  // 토큰 값 확인
  console.log(`TOKEN length: ${TOKEN ? TOKEN.length : 'null'}`);
  console.log(`TOKEN first 20: ${TOKEN ? TOKEN.substring(0, 20) : 'null'}`);
  console.log(`TOKEN last 10: ${TOKEN ? TOKEN.substring(TOKEN.length - 10) : 'null'}`);

  const headerValue = `Bearer ${TOKEN}`;
  console.log(`Header length: ${headerValue.length}`);

  const headers = { 'Authorization': headerValue };
  const res = http.get(`${BASE_URL}/api/community`, { headers });
  console.log(`status: ${res.status}`);
  console.log(`body: ${res.body.substring(0, 300)}`);
}
