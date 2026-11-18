UPDATE cliente SET telefone = regexp_replace(telefone, '\D', '', 'g');
