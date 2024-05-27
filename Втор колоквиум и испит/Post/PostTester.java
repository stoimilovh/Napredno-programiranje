//Да се имплементира класа Post во која ќе се чуваат информациите за објава на една социјална
//        мрежа. Во класата да се имплементираат следните методи:
//
//        Конструктор Post(String username, String postContent)
//        void addComment (String username, String commentId, String content, String replyToId) -
//        метод за додавање на коментар со ИД commentId и содржина content од корисникот со корисничко
//        име username. Коментарот може да биде директно на самата објава
//        (replyToId=null во таа ситуација) или да биде reply на веќе постоечки коментар/reply. **
//        void likeComment (String commentId) - метод за лајкнување на коментар.
//        String toString() - toString репрезентација на една објава во форматот прикажан подолу.
//        Коментарите се листаат во опаѓачки редослед според бројот на лајкови
//        (во вкупниот број на лајкови се сметаат и лајковите на replies на коментарите,
//        како и на replies na replies итн.)
//        ** Решенијата кои ќе овозможат само коментари на објавата ќе бидат оценети со 50% од поените.
//        Истото тоа е рефлектирано во тест примерите (50% од тест примерите се со коментари само на
//        објавата, 50% се со вгнездени коментари и replies)

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Comment {
    private final String username;
    private final String commentId;
    private final String content;
    private int likes;
    final List<Comment> replies;

    public Comment(String username, String commentId, String content) {
        this.username = username;
        this.commentId = commentId;
        this.content = content;
        this.likes = 0;
        this.replies = new ArrayList<>();
    }
    Comparator<Comment> comparator = (c1, c2) -> Integer.compare(getTotalLikes(c1), getTotalLikes(c2));
    public String getCommentId() {
        return commentId;
    }
    public int getLikes() {
        return likes;
    }
    public void like() {
        likes++;
    }
    public void addReply(Comment reply) {
        replies.add(reply);
    }
    private int getTotalLikes(Comment comment) {
        int totalLikes = comment.getLikes();
        for (Comment reply : comment.replies) {
            totalLikes += getTotalLikes(reply);
        }
        return totalLikes;
    }
    public String toString(int depth) {
        String intend = IntStream.range(0, depth).mapToObj(i -> " ").collect(Collectors.joining(""));

        String repliesDetails = replies.stream()
                .sorted(comparator.reversed())
                .map(comment -> comment.toString(depth + 4))
                .collect(Collectors.joining(""));

        return String.format("%sComment: %s\n%sWritten by: %s\n%sLikes: %d\n%s",
                intend, content, intend, username, intend, likes, repliesDetails);
    }
}

class Post {
    private final String username;
    private final String postContent;
    private final List<Comment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.comments = new ArrayList<>();
    }
    Comparator<Comment> comparator = (c1, c2) -> Integer.compare(getTotalLikes(c1), getTotalLikes(c2));
    public void addComment(String username, String commentId, String content, String replyToId) {
        Comment reply = new Comment(username, commentId, content);
        if (replyToId == null) {
            comments.add(reply);
        } else {
            Comment replayedComment = findComment(replyToId, comments);
            replayedComment.addReply(reply);
        }
    }
    public void likeComment(String commentId) {
        Comment comment = findComment(commentId, comments);
        comment.like();
    }
    private Comment findComment(String commentId, List<Comment> comments) {

        return comments.stream().sorted(comparator.reversed())
                .filter(comment -> comment.getCommentId().equals(commentId))
                .findFirst()
                .orElseGet(() -> comments.stream().map(comment -> findComment(commentId, comment.replies))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null));
    }
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Post: %s\nWritten by: %s\nComments:\n",
                postContent, username));

        String commentsDetails = comments.stream().sorted(comparator.reversed())
                .map(comment -> comment.toString(8))
                .collect(Collectors.joining(""));

        result.append(commentsDetails);
        return result.toString();
    }
    private int getTotalLikes(Comment comment) {
        int totalLikes = comment.getLikes();
        for (Comment reply : comment.replies) {
            totalLikes += getTotalLikes(reply);
        }
        return totalLikes;
    }
}


public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }
        }
    }
}