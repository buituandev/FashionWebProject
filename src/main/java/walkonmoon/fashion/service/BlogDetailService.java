package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.BlogDetail;
import walkonmoon.fashion.repository.BlogDetailRepository;

import java.util.Optional;

@Service
@Transactional
public class BlogDetailService {
    @Autowired
    private BlogDetailRepository blogDetailRepository;

    public BlogDetail getBlogDetailByBlogID(int id) {
        Optional<BlogDetail> blogDetail = blogDetailRepository.findByBlogID(id);
        return blogDetail.orElse(null);
    }
}
