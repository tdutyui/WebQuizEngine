package engine;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@Validated
public class Controller {

    int count;
    Map<Integer, Quiz> quizzes = new HashMap<>();

    @PostMapping("/api/quizzes")
    public Quiz createQuiz(@RequestBody @Valid Quiz quiz) {
        if (quizzes == null) {
            count = 1;
        } else {
            count++;
        }
        quiz.setId(count);
        quizzes.put(count, quiz);
        return quiz;
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuizByID(@PathVariable int id) {
        if (!quizzes.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizzes.get(id);
    }

    @GetMapping("/api/quizzes")
    public Collection<Quiz> getAllQuizzes() {
        return quizzes.values();
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResultMessage getResult(@RequestBody Answer answer, @PathVariable int id) {
        Quiz quiz = getQuizByID(id);
        if (quiz.getAnswer() == null) {
            quiz.setAnswer(new int[]{});
        }
        if (Arrays.equals(answer.getAnswer(), quiz.getAnswer())) {
            return new ResultMessage(true, "Congratulations, you're right!");
        } else {
            return new ResultMessage(false, "Wrong answer! Please, try again.");
        }
    }
}
