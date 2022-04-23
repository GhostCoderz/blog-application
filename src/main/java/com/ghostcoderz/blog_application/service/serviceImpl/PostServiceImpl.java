package com.ghostcoderz.blog_application.service.serviceImpl;

import com.ghostcoderz.blog_application.entity.Category;
import com.ghostcoderz.blog_application.entity.Post;
import com.ghostcoderz.blog_application.entity.User;
import com.ghostcoderz.blog_application.exceptions.ResourceNotFoundException;
import com.ghostcoderz.blog_application.payload.PostDto;
import com.ghostcoderz.blog_application.payload.PostResponse;
import com.ghostcoderz.blog_application.repository.CategoryRepo;
import com.ghostcoderz.blog_application.repository.PostRepo;
import com.ghostcoderz.blog_application.repository.UserRepo;
import com.ghostcoderz.blog_application.service.serviceInterface.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepo postRepo, UserRepo userRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(
            PostDto postDto,
            Long userId,
            Integer categoryId
    ) {

        // Getting user details
        User user = this.userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id" , userId));

        // Getting category details
        Category category = this.categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Category", "id" , categoryId.longValue()));

        Post post = dtoToPost(postDto);
        post.setImageName("default.png");
        post.setCreateDt(new Date());
        post.setCategory(category);
        post.setUser(user);
        this.postRepo.save(post);

        return postToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId) {
        Post post= dtoToPost(postDto);
        Post postInDB = postRepo.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Post", "id" , postId));
        postInDB.setTitle(post.getTitle());
        postInDB.setContent(post.getContent());
        postInDB.setImageName(post.getImageName());
        Post updatedPost = this.postRepo.save(postInDB);

        return postToDto(updatedPost);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post postInDB = postRepo.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Post", "id" , postId));
        return postToDto(postInDB);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> posts = this.postRepo.findAll(pageable);

        List<PostDto> content = posts.stream()
                .map(this::postToDto).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNumber(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLastPage(posts.isLast());

        return postResponse;

    }

    @Override
    public void deletePost(Long postId) {
        try {
            this.postRepo.deleteById(postId);
        } catch(Exception e) {
            throw new ResourceNotFoundException(
                    "Post", "id", postId);
        }
    }

    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Category", "id", categoryId.longValue()));
        List<Post> posts = this.postRepo.findByCategory(category);
        List<PostDto> postDtos = posts.stream()
                .map(this::postToDto).
                collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getPostByUser(Long userId) {
        User user = this.userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", userId));
        List<Post> posts = this.postRepo.findByUser(user);
        List<PostDto> postDtos = posts.stream()
                .map(this::postToDto).
                collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        return null;
    }

    private PostDto postToDto(Post post){
        return this.modelMapper.map(post, PostDto.class);
    }

    private Post dtoToPost(PostDto postDto){
        return this.modelMapper.map(postDto, Post.class);
    }

}
