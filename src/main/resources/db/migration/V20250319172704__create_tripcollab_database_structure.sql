-- Estrutura corrigida do banco de dados para o TripCollab

-- Tabela de Usuários
CREATE TABLE tb_users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Funções (Roles) com valores fixos
CREATE TABLE tb_roles (
    role_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Inserindo os papéis fixos
INSERT INTO tb_roles (role_id, name) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_id, name) VALUES (2, 'USER');

-- Tabela de Usuários e suas Funções (Relacionamento N:N)
CREATE TABLE tb_user_roles (
    user_id UUID REFERENCES tb_users(user_id) ON DELETE CASCADE,
    role_id INTEGER REFERENCES tb_roles(role_id) ON DELETE RESTRICT,
    PRIMARY KEY (user_id, role_id)
);

-- Tabela de Postagens
CREATE TABLE tb_posts (
    post_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Imagens dos Posts
CREATE TABLE tb_post_images (
    image_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL REFERENCES tb_posts(post_id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Comentários
CREATE TABLE tb_comments (
    comment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES tb_posts(post_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Posts Salvos
CREATE TABLE tb_savedposts (
    savedpost_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES tb_posts(post_id) ON DELETE CASCADE,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Curtidas
CREATE TABLE tb_likes (
    like_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES tb_posts(post_id) ON DELETE CASCADE,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Seguidores (Relacionamento N:N entre usuários)
CREATE TABLE tb_followers (
    following_user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    followed_user_id UUID NOT NULL REFERENCES tb_users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (following_user_id, followed_user_id)
);
