-- Funções para Likes
CREATE OR REPLACE FUNCTION inc_like_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET like_count = like_count + 1
    WHERE post_id = NEW.post_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dec_like_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET like_count = like_count - 1
    WHERE post_id = OLD.post_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Triggers para Likes
CREATE TRIGGER update_like_count_on_insert
AFTER INSERT ON tb_likes
FOR EACH ROW
EXECUTE FUNCTION inc_like_count();

CREATE TRIGGER update_like_count_on_delete
AFTER DELETE ON tb_likes
FOR EACH ROW
EXECUTE FUNCTION dec_like_count();

-- Funções para Comentários
CREATE OR REPLACE FUNCTION inc_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET comment_count = comment_count + 1
    WHERE post_id = NEW.post_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dec_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET comment_count = comment_count - 1
    WHERE post_id = OLD.post_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Triggers para Comentários
CREATE TRIGGER update_comment_count_on_insert
AFTER INSERT ON tb_comments
FOR EACH ROW
EXECUTE FUNCTION inc_comment_count();

CREATE TRIGGER update_comment_count_on_delete
AFTER DELETE ON tb_comments
FOR EACH ROW
EXECUTE FUNCTION dec_comment_count();

-- Funções para Saves
CREATE OR REPLACE FUNCTION inc_save_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET save_count = save_count + 1
    WHERE post_id = NEW.post_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dec_save_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tb_posts
    SET save_count = save_count - 1
    WHERE post_id = OLD.post_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Triggers para Saves
CREATE TRIGGER update_save_count_on_insert
AFTER INSERT ON tb_savedposts
FOR EACH ROW
EXECUTE FUNCTION inc_save_count();

CREATE TRIGGER update_save_count_on_delete
AFTER DELETE ON tb_savedposts
FOR EACH ROW
EXECUTE FUNCTION dec_save_count();