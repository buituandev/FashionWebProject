package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Blog;
import walkonmoon.fashion.repository.BlogRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getListBlogs() {
        return (List<Blog>) blogRepository.findAll();
    }

    public Blog getBlogById(int id) {
        Optional<Blog> blog = blogRepository.findById(id);
        return blog.orElse(null);
    }
    public void saveBlog(Blog blog){
        blogRepository.save(blog);
    }
    public void deleteBlog(int id){
        blogRepository.deleteById(id);
    }

}
