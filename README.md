# OpenAI Code Review

[![Java](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.12-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![OpenAI API](https://img.shields.io/badge/OpenAI_API-v1-412991?logo=openai&logoColor=white)](https://openai.com/)
[![WeChat](https://img.shields.io/badge/WeChat-Integration-07C160?logo=wechat&logoColor=white)](https://www.wechat.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-2088FF?logo=github-actions&logoColor=white)](https://github.com/features/actions)

An automated code review system powered by OpenAI that integrates with Git and WeChat for notifications.

## Features

- 🤖 AI-Powered Code Review - Automated code analysis using OpenAI
- 📊 Git Integration - Analyzes git diff and commits
- 💬 WeChat Notifications - Instant review notifications via WeChat
- 🔄 GitHub Actions - Automated workflow integration
- 📝 Review Logging - Stores review history in a separate repository

## Tech Stack

- **Language:** Java 11
- **Framework:** Spring Boot 2.7.12
- **Build Tool:** Maven
- **AI Integration:** OpenAI
- **Version Control:** JGit
- **Messaging:** WeChat Template Messages
- **CI/CD:** GitHub Actions

## Getting Started

### Prerequisites

- Java 11+
- Maven
- GitHub account
- WeChat Official Account
- OpenAI API key

### Installation

1. Clone the repository

```bash
git clone git@github.com:Zeyu-Chen/AI-Podcast-App.git
cd AI-Code-Review
```

2. Build with Maven

```bash
mvn clean install
```

### Environment Variables

Configure the following environment variables in your GitHub repository's Settings > Secrets and Variables > Actions:

```bash
# GitHub
GITHUB_REVIEW_LOG_URI=       # Review log repository URI
GITHUB_TOKEN=                # GitHub personal access token
COMMIT_PROJECT=              # Target project name
COMMIT_BRANCH=               # Target branch name
COMMIT_AUTHOR=               # Commit author
COMMIT_MESSAGE=              # Commit message

# WeChat
WECHAT_APPID=                # WeChat Official Account AppID
WECHAT_SECRET=               # WeChat Official Account Secret
WECHAT_TOUSER=               # WeChat user OpenID
WECHAT_TEMPLATE_ID=          # Message template ID

# OpenAI
OPENAI_APIHOST=              # OpenAI API endpoint
OPENAI_APIKEYSECRET=         # OpenAI API key
```

## Project Structure

```
openai-code-review/
├── openai-code-review-sdk/     # Core SDK module
│   ├── domain/                 # Domain logic
│   ├── infrastructure/         # External services
│   └── types/                  # Utility classes
├── openai-code-review-test/    # Test module
└── .github/                    # GitHub Actions workflows
```

## Usage

The system can be used in two ways:

1. As a GitHub Action:

```yaml
name: Code Review
on: [push, pull_request]
jobs:
  review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Code Review
        uses: ./
        with:
          github-token: ${{ secrets.GITHUB_TOKEN }}
```

2. As a Java library:

```java
OpenAICodeReview review = new OpenAICodeReview();
review.exec();
```
