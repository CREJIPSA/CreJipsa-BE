import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';
import { open } from 'k6/experimental/fs';

// 커스텀 메트릭
const listLatency = new Trend('community_list_duration', true);
const detailLatency = new Trend('community_detail_duration', true);
const commentLatency = new Trend('comment_list_duration', true);
const filterLatency = new Trend('community_filter_duration', true);
const errorRate = new Rate('errors');
const requestCount = new Counter('total_requests');

export const options = {
  stages: [
    { duration: '10s', target: 50 },
    { duration: '30s', target: 100 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
    errors: ['rate<0.01'],
  },
};

const BASE_URL = 'http://localhost:8080';
const TOKEN = __ENV.TOKEN;
const COMMUNITY_IDS = [1, 3, 4, 5, 6, 8, 10, 11];
const FIELDS = ['RECOMMEND', 'TIP', 'COLLAB'];

const headers = {
  'Authorization': `Bearer ${TOKEN}`,
  'Content-Type': 'application/json',
};

export default function () {
  const communityId = COMMUNITY_IDS[Math.floor(Math.random() * COMMUNITY_IDS.length)];
  const field = FIELDS[Math.floor(Math.random() * FIELDS.length)];

  // 1. 전체 목록 조회
  {
    const res = http.get(`${BASE_URL}/api/community`, { headers });
    listLatency.add(res.timings.duration);
    requestCount.add(1);
    const ok = check(res, { 'community list 200': (r) => r.status === 200 });
    if (!ok) errorRate.add(1); else errorRate.add(0);
  }

  sleep(0.1);

  // 2. 필드별 목록 조회
  {
    const res = http.get(`${BASE_URL}/api/community/filter?field=${field}`, { headers });
    filterLatency.add(res.timings.duration);
    requestCount.add(1);
    const ok = check(res, { 'filter list 200': (r) => r.status === 200 });
    if (!ok) errorRate.add(1); else errorRate.add(0);
  }

  sleep(0.1);

  // 3. 커뮤니티 상세 조회
  {
    const res = http.get(`${BASE_URL}/api/community/${communityId}`, { headers });
    detailLatency.add(res.timings.duration);
    requestCount.add(1);
    const ok = check(res, { 'community detail 200': (r) => r.status === 200 });
    if (!ok) errorRate.add(1); else errorRate.add(0);
  }

  sleep(0.1);

  // 4. 댓글 목록 조회
  {
    const res = http.get(`${BASE_URL}/api/comment/community/${communityId}`, { headers });
    commentLatency.add(res.timings.duration);
    requestCount.add(1);
    const ok = check(res, { 'comment list 200': (r) => r.status === 200 });
    if (!ok) errorRate.add(1); else errorRate.add(0);
  }

  sleep(0.2);
}
