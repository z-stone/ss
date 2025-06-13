-- 데이터베이스 초기화 스크립트
-- 필요한 확장 프로그램 설치
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- design_component 테이블 생성
CREATE TABLE IF NOT EXISTS TB_COMPONENT_INFO (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    css TEXT NOT NULL,
    html TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_component_info_description ON TB_COMPONENT_INFO(description);

-- 초기 데이터 삽입
INSERT INTO TB_COMPONENT_INFO (id, description, css, html) VALUES
(
    '77be6ce1-75ff-45f3-9b93-3319fafe2dee',
    '플로우 차트 컨테이너',
    '.flow-chart {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem 0;
  max-width: 800px;
  margin: 0 auto;
}

.flow-row {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-bottom: 2.5rem;
  position: relative;
  z-index: 1;
  gap: 20px;
}',
    '<div class="flow-chart">
  <!-- 여기에 노드들이 배치됩니다 -->
  <div class="flow-row"><!-- 첫 번째 행의 노드들 --></div>
  <div class="flow-row"><!-- 두 번째 행의 노드들 --></div>
  <!-- 추가 행... -->
</div>'
),
(
    '12ebc127-9e1b-45ec-a041-67b3928a3fae',
    '시작 노드',
    '.flow-item.start {
  background: linear-gradient(135deg, #4cc9f0, #4361ee);
  color: white;
  width: 220px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.flow-item.start:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}',
    '<div class="flow-item start">
  <div class="flow-icon">🚀</div>
  <div class="flow-title">시작</div>
  <div class="flow-description">서비스 접속</div>
</div>'
),
(
    '493c6798-8d70-4ae7-bf8d-2bce6a22238e',
    '종료 노드',
    '.flow-item.end {
  background: linear-gradient(135deg, #f72585, #7209b7);
  color: white;
  width: 220px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.flow-item.end:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
.pulse {
  animation: pulse 2s infinite;
}',
    '<div class="flow-item end pulse">
  <div class="flow-icon">🎉</div>
  <div class="flow-title">완료</div>
  <div class="flow-description">서비스 이용 시작</div>
</div>'
),
(
    '4c022289-a881-4637-a34e-22c4c2869dfd',
    '프로세스 노드 (파란색)',
    '.flow-item.process {
  background: white;
  width: 220px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.theme-blue {
  border: 2px solid #4361ee;
  color: #4361ee;
}
.flow-item.process:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}',
    '<div class="flow-item process theme-blue">
  <div class="flow-icon">📝</div>
  <div class="flow-title">회원가입 양식</div>
  <div class="flow-description">사용자 정보 입력</div>
</div>'
),
(
    'b6a949a9-55e0-442e-8c86-5bb6ef22ee6f',
    '프로세스 노드 (분홍색)',
    '.flow-item.process {
  background: white;
  width: 220px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.theme-pink {
  border: 2px solid #f72585;
  color: #f72585;
}
.flow-item.process:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}',
    '<div class="flow-item process theme-pink">
  <div class="flow-icon">⚠️</div>
  <div class="flow-title">오류 처리</div>
  <div class="flow-description">정보 재입력</div>
</div>'
),
(
    '883388cb-0fc2-4e31-9ac8-aa962b4e76ec',
    '프로세스 노드 (청록색)',
    '.flow-item.process {
  background: white;
  width: 220px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.theme-teal {
  border: 2px solid #4cc9f0;
  color: #4cc9f0;
}
.badge {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: #7209b7;
  color: white;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}',
    '<div class="flow-item process theme-teal">
  <div class="flow-icon">✅</div>
  <div class="flow-title">계정 생성</div>
  <div class="flow-description">데이터베이스 저장</div>
  <div class="badge">1</div>
</div>'
),
(
    'a2b02a7b-fd71-41b3-ab8b-ce62002ca093',
    '결정 노드',
    '.flow-item.decision {
  background: white;
  border: 2px solid #f72585;
  transform: rotate(45deg);
  width: 140px;
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
  margin-top: -20px;
  color: #f72585;
}
.flow-item.decision .flow-content {
  transform: rotate(-45deg);
  width: 100px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.flow-item.decision:hover {
  transform: rotate(45deg) translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}',
    '<div class="flow-item decision">
  <div class="flow-content">
    <div class="flow-title">검증</div>
    <div class="flow-description">유효성 확인</div>
  </div>
</div>'
),
(
    '564b9172-1246-45fe-85ab-42939904ad98',
    '병렬 노드 (보라색)',
    '.flow-item.parallel {
  background: white;
  border: 2px solid #3a0ca3;
  width: 240px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
}
.theme-purple {
  border-color: #3a0ca3;
  color: #3a0ca3;
}
.flow-item.parallel:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}',
    '<div class="flow-item parallel theme-purple">
  <div class="flow-icon">📧</div>
  <div class="flow-title">이메일 인증</div>
  <div class="flow-description">확인 링크 발송 및 처리</div>
</div>'
),
(
    '2776c9a4-ea9f-479f-8e2c-e4acdef81248',
    '실선 화살표 (수직)',
    'svg {
  display: block;
  margin: 0 auto;
}',
    '<svg width="10" height="50">
  <path d="M5,0 L5,40" stroke="#dfe3fa" stroke-width="3" fill="none" />
  <polygon points="5,50 0,40 10,40" fill="#dfe3fa" />
</svg>'
),
(
    'fb48eac0-0b2b-45da-b1a8-4c7328981069',
    '실선 화살표 (수평)',
    'svg {
  display: block;
  margin: 0 auto;
}',
    '<svg width="50" height="10">
  <path d="M0,5 L40,5" stroke="#dfe3fa" stroke-width="3" fill="none" />
  <polygon points="50,5 40,0 40,10" fill="#dfe3fa" />
</svg>'
),
(
    'fe51fc88-eb3e-49bc-9d25-d322d4cb865d',
    '점선 화살표 (수평)',
    'svg {
  display: block;
  margin: 0 auto;
}',
    '<svg width="50" height="10">
  <path d="M0,5 L40,5" stroke="#dfe3fa" stroke-width="3" fill="none" stroke-dasharray="5,3" />
  <polygon points="50,5 40,0 40,10" fill="#dfe3fa" />
</svg>'
),
(
    '60ffcc18-bd45-4343-926e-84646d260ced',
    'Yes/No 레이블',
    '.flow-label {
  font-size: 14px;
  font-weight: 600;
  padding: 3px 10px;
  border-radius: 4px;
  display: inline-block;
  margin: 5px;
}
.flow-label.yes {
  color: #7209b7;
}
.flow-label.no {
  color: #7209b7;
}',
    '<div class="flow-label yes">Yes</div>
<div class="flow-label no">No</div>'
),
(
    '092ea76d-914f-4f1d-aa6f-09b3e4381b07',
    '공통 스타일',
    ':root {
  --primary-color: #4361ee;
  --secondary-color: #3a0ca3;
  --accent-color: #7209b7;
  --success-color: #4cc9f0;
  --warning-color: #f72585;
  --light-color: #f8f9fa;
  --dark-color: #212529;
  --shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
  --transition: all 0.3s ease;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: ''Pretendard'', ''Apple SD Gothic Neo'', sans-serif;
  background-color: #f5f7ff;
  color: var(--dark-color);
  line-height: 1.6;
  padding: 2rem;
}

.flow-icon {
  margin-bottom: 0.5rem;
  font-size: 1.6rem;
}

.flow-title {
  font-weight: 600;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  word-break: keep-all;
  width: 90%;
  line-height: 1.3;
}

.flow-description {
  font-size: 0.9rem;
  opacity: 0.8;
  word-break: keep-all;
  width: 90%;
  line-height: 1.3;
}',
    '<!-- 이 컴포넌트는 HTML 대신 전체 CSS 변수와 기본 스타일을 제공합니다 -->'
),
(
    '6369626f-2404-4b8b-a9ff-f10127963de5',
    '분기 영역',
    '.flow-split {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin: 20px 0;
}

.flow-column {
  display: flex;
  flex-direction: column;
  align-items: center;
}',
    '<div class="flow-split">
  <div class="flow-column">
    <!-- Yes 경로 컨텐츠 -->
  </div>
  <div class="flow-column">
    <!-- No 경로 컨텐츠 -->
  </div>
</div>'
);

COMMIT; 