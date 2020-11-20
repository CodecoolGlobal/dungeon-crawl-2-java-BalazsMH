DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    current_map text NOT NULL,
    stored_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL,
    save_name text NOT NULL
);

DROP TABLE IF EXISTS public.pokemon;
CREATE TABLE public.pokemon(
    id serial NOT NULL PRIMARY KEY,
    player_id integer NOT NULL,
    pokeid integer NOT NULL,
    game_level integer,
    pokeHealth integer NOT NULL,
    pokeDamage integer NOT NULL,
    pokeName text NOT NULL,
    x integer,
    y integer,
    cellType text
);


DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player (
    id serial NOT NULL PRIMARY KEY,
    player_name text NOT NULL,
    god_mode boolean NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL,
    game_level integer NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.pokemon
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
