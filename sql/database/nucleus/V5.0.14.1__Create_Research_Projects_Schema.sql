CREATE TABLE research_projects(
id bigserial NOT NULL PRIMARY KEY, 
title text NOT NULL, 
summary text NOT NULL,
category text,
description text,   
teams text[],
publications jsonb,  
data text[],
created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);
