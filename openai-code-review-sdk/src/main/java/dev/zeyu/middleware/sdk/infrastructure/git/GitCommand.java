package dev.zeyu.middleware.sdk.infrastructure.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.zeyu.middleware.sdk.types.utils.RandomStringUtils;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GitCommand {

    private final Logger logger = LoggerFactory.getLogger(GitCommand.class);
    private final String githubReviewLogUri;
    private final String githubToken;
    private final String project;
    private final String branch;
    private final String author;
    private final String message;

    public GitCommand(String githubReviewLogUri, String githubToken, String project, String branch, String author, String message) {
        this.githubReviewLogUri = githubReviewLogUri;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    public String diff() throws Exception {
        String latestCommitHash = getLatestCommitHash();
        Process diffProcess = getDiffProcess(latestCommitHash);
        return readProcessOutput(diffProcess);
    }

    private String getLatestCommitHash() throws Exception {
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        logProcessBuilder.directory(new File("."));
        Process logProcess = logProcessBuilder.start();

        String hash = readProcessOutput(logProcess).trim();
        logProcess.waitFor();
        return hash;
    }

    private Process getDiffProcess(String latestCommitHash) throws Exception {
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        diffProcessBuilder.directory(new File("."));
        return diffProcessBuilder.start();
    }

    private String readProcessOutput(Process process) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    public String commitAndPush(String recommend) throws Exception {
        Git git = cloneRepository();
        String fileName = createAndWriteFile(recommend);
        commitAndPushChanges(git, fileName);
        return generateFileUrl(fileName);
    }

    private Git cloneRepository() throws Exception {
        return Git.cloneRepository()
                .setURI(githubReviewLogUri + ".git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""))
                .call();
    }

    private String createAndWriteFile(String content) throws Exception {
        String dateFolderName = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        Path dateFolder = Paths.get("repo", dateFolderName);
        Files.createDirectories(dateFolder);

        String fileName = generateFileName();
        Path filePath = dateFolder.resolve(fileName);
        Files.write(filePath, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return dateFolderName + "/" + fileName;
    }

    private String generateFileName() {
        return project + "-" + branch + "-" + author + System.currentTimeMillis() + "-" + RandomStringUtils.randomNumeric(4) + ".md";
    }

    private void commitAndPushChanges(Git git, String fileName) throws Exception {
        git.add().addFilepattern(fileName).call();
        git.commit().setMessage("add code review new file " + fileName).call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, "")).call();
        logger.info("openai-code-review git commit and push done! {}", fileName);
    }

    private String generateFileUrl(String fileName) {
        return githubReviewLogUri + "/blob/master/" + fileName;
    }

    public String getProject() { return project; }
    public String getBranch() { return branch; }
    public String getAuthor() { return author; }
    public String getMessage() { return message; }
}