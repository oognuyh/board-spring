package com.oognuyh.board.global.config;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import com.oognuyh.board.post.model.Comment;
import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.post.repository.CommentRepository;
import com.oognuyh.board.post.repository.PostRepository;
import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@Profile("dev")
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		log.info("Insert fake data into database");

		User admin = userRepository.save(User.builder()
			.email("admin")
			.password(passwordEncoder.encode("admin"))
			.name("admin")
			.phoneNumber("01012345678")
			.role(Roles.ROLE_ADMIN)
			.build());

		User user = userRepository.save(User.builder()
			.email("user")
			.password(passwordEncoder.encode("user"))
			.name("user")
			.phoneNumber("01012345678")
			.role(Roles.ROLE_USER)
			.build());
		
		List<Post> posts = postRepository.saveAll(Arrays.asList(
			Post.builder().title("test 1").content("test content").user(user).build(),
			Post.builder().title("test 2").content("test content").user(user).build(),
			Post.builder().title("test 3").content("test content").user(admin).build(),
			Post.builder().title("test 4").content("test content").user(user).build(),
			Post.builder().title("test 5").content("test content").user(user).build(),
			Post.builder().title("test 6").content("test content").user(user).build(),
			Post.builder().title("test 7").content("test content").user(user).build(),
			Post.builder().title("test 8").content("test content").user(user).build(),
			Post.builder().title("test 9").content("test content").user(user).build(),
			Post.builder().title("test 10").content("test content").user(user).build(),
			Post.builder().title("test 11").content("test content").user(user).build(),
			Post.builder().title("test 12").content("test content").user(user).build(),
			Post.builder().title("test 13").content("test content").user(user).build(),
			Post.builder().title("test 14").content("test content").user(user).build(),
			Post.builder().title("test 15").content("test content").user(user).build(),
			Post.builder().title("test 16").content("test content").user(user).build(),
			Post.builder().title("test 17").content("test content").user(user).build(),
			Post.builder().title("test 18").content("test content").user(user).build(),
			Post.builder().title("test 19").content("test content").user(user).build(),
			Post.builder().title("test 20").content("test content").user(user).build(),
			Post.builder().title("test 21").content("test content").user(user).build(),
			Post.builder().title("test 22").content("test content").user(user).build(),
			Post.builder().title("test 23").content("test content").user(user).build(),
			Post.builder().title("test 24").content("test content").user(user).build(),
			Post.builder().title("test 26").content("test content").user(user).build(),
			Post.builder().title("test 27").content("test content").user(user).build(),
			Post.builder().title("test 28").content("test content").user(user).build()
		));

		List<Comment> comments = Arrays.asList(
			Comment.builder().content("test content").post(posts.get(26)).user(user).build(),
			Comment.builder().content("test content").post(posts.get(26)).user(user).build(),
			Comment.builder().content("test content").post(posts.get(26)).user(admin).build()
		);

		commentRepository.saveAll(comments);
	}
}