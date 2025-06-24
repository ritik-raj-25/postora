package com.postora.postora_backend.services;

import java.util.List;

import com.postora.postora_backend.dtos.BookmarkDto;

public interface BookmarkService {
	void addBookmarkToPost(Long postId);
	void removeBookmarkFromPost(Long postId);
	List<BookmarkDto> getBookmarkDetailsOfUser();
}
