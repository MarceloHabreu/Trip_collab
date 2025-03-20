-- Criando a função para atualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Criando o trigger na tabela tb_posts
CREATE TRIGGER trigger_update_updated_at
BEFORE UPDATE ON tb_posts
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
