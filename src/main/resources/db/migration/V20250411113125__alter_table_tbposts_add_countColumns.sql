ALTER TABLE tb_posts
ADD COLUMN like_count INT DEFAULT 0,
ADD COLUMN comment_count INT DEFAULT 0,
ADD COLUMN save_count INT DEFAULT 0;