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

DROP TABLE IF EXISTS public.inventory;
CREATE TABLE public.inventory (
    id serial NOT NULL PRIMARY KEY,
    player_id INTEGER NOT NULL,
    health_potion_number INTEGER NOT NULL,
    poke_ball_number INTEGER NOT NULL,
    has_key boolean NOT NULL,
    active_pokemon_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS public.lootbox;
CREATE TABLE public.lootbox (
    id serial NOT NULL PRIMARY KEY,
    player_id INTEGER NOT NULL,
    health_potion_number INTEGER NOT NULL,
    poke_ball_number INTEGER NOT NULL,
    x INTEGER,
    y INTEGER,
    level INTEGER NOT NULL,
    lootbox_id INTEGER NOT NULL
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

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.lootbox
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);