package com.hanghae.be_h010gram.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae.be_h010gram.domain.member.entity.Member;
import com.hanghae.be_h010gram.domain.post.dto.PostRequestDto;
import com.hanghae.be_h010gram.exception.CustomException;
import com.hanghae.be_h010gram.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

import static com.hanghae.be_h010gram.exception.ExceptionEnum.POST_CONTENT_NOT_FOUND;
import static com.hanghae.be_h010gram.exception.ExceptionEnum.USER_NOT_FOUND;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    @JsonBackReference
    private List<Comment> comments;

    @ColumnDefault("0")
    private int postLike;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<PostLike> postLikes;

    public Post(PostRequestDto postRequestDto, Member member) {
        if (member.getId() == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (postRequestDto.getContent() == null || postRequestDto.getContent().isEmpty()) {
            throw new CustomException(POST_CONTENT_NOT_FOUND);
        }

        this.content = postRequestDto.getContent();
        this.member = member;
    }

    public void update(PostRequestDto postRequestDto) {
        this.content = postRequestDto.getContent();
    }

    public void updateLike(boolean likeOrDislike) {
        this.postLike = likeOrDislike ? this.postLike + 1 : this.postLike - 1;
    }

}