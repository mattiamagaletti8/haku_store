--
-- PostgreSQL database dump
--

\restrict CjiFvM84FGfi75EpLJNzIYc7Qecimw0sG9Co8Ii4DEfz3hU7y1BG1Vt9cZDV7WX

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: coupon; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.coupon (id_coupon, codice, tipologia, valore, data_inizio, data_fine, is_attivo) VALUES (2, 'GERARD10', 'PERCENTUALE', 10.00, '2020-01-01 00:00:00', '2030-01-01 23:59:59', true);


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.utente (id_utente, nome, cognome, email, password, telefono, ruolo) VALUES (1, 'Mario', 'Rossi', 'mario.frontend@test.it', '$2a$10$j9RGaGVRyH.9ZQzNFN5HberGAC0pEtRI04IiKr0RPUIMDlcUM16bK', NULL, 'ADMIN');
INSERT INTO public.utente (id_utente, nome, cognome, email, password, telefono, ruolo) VALUES (2, 'mattia', 'magaletti', 'mattiamagaletti8@gmail.com', '$2a$10$VNFBONWSXQn11MthKDWh6.khWvPDJNZ/.3Cqk5fY5tbRJzBiEMkYy', '', 'CLIENTE');
INSERT INTO public.utente (id_utente, nome, cognome, email, password, telefono, ruolo) VALUES (6, 'Sara', 'DB', 'saradb@gmail.com', '$2a$10$Bv91UlqjqElJpQJK/Fc4vePj3jC.GXsB9T5/Wc3uLiX3RS2klHpme', NULL, 'CLIENTE');
INSERT INTO public.utente (id_utente, nome, cognome, email, password, telefono, ruolo) VALUES (10, 'pier', 'rolli', 'pier.rolli@gmail.com', '$2a$10$M2ebL5fY9yUX7//AmeTYk.RroUO3M2Pxdn69UKISD9UWNlJy1r0cm', NULL, 'CLIENTE');


--
-- Data for Name: carrello; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.carrello (id_carrello, id_utente, id_coupon, data_creazione) VALUES (1, 1, NULL, '2026-07-13 16:57:47.693859');
INSERT INTO public.carrello (id_carrello, id_utente, id_coupon, data_creazione) VALUES (2, 2, NULL, '2026-07-13 17:18:53.627735');


--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.categoria (id_categoria, nome, immagine) VALUES (1, 'Integratori', 'categoria_1.jpeg');
INSERT INTO public.categoria (id_categoria, nome, immagine) VALUES (2, 'equipaggiamento per palestra', 'categoria_2.jpeg');
INSERT INTO public.categoria (id_categoria, nome, immagine) VALUES (3, 'Alimentari', NULL);


--
-- Data for Name: prodotto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (1, 1, 'Proteine Whey', 'Proteine in polvere gusto cioccolato', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (2, 1, 'Omega 3', 'ottimo per la salute cardiovascolare', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (3, 1, 'Creatina', 'Integratore per palestra', 'Hakustore', 'prodotto_3.jpeg', '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (4, 1, 'Vitamine', 'diversi tipi', 'Prozis', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (6, 1, 'Farina d''Avena', 'Farina d''avena in polvere, ideale per ricette proteiche', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (7, 1, 'Whey Isolate', 'Proteine isolate ad alto contenuto proteico, bassi carboidrati', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (8, 1, 'Whey Vegan', 'Proteine vegetali da piselli e riso, gusto cremoso', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (9, 1, 'Whey Vegan Isolate', 'Proteine vegetali isolate, massima purezza', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (10, 3, 'Barretta Proteica', 'Barretta proteica 22g proteine, low sugar, box da 12', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (14, 1, 'Melatonina', 'N-Acetyl-5-Methoxytryptamine, supporto al sonno', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (15, 1, 'Ashwagandha', 'Withania Somnifera Extract, erba adattogena', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (16, 1, 'Integratore Magnesio e Potassio', 'Compresse di Magnesio e Potassio Citrato', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (17, 3, 'Salsa Barbecue', 'Salsa barbecue premium blend, gusto smoky, senza zuccheri, gluten free', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (18, 2, 'Asciugamano HakuStore', 'Asciugamano in microfibra ad asciugatura rapida, logo ricamato', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (19, 2, 'Shaker HakuStore', 'Shaker 700ml con mixer a molla, logo HakuStore', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (20, 2, 'Polsini da Sollevamento', 'Polsini imbottiti con fascia regolabile, per sollevamento pesi', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (21, 2, 'Cintura da Powerlifting', 'Cintura in pelle con fibbia a leva, per powerlifting', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (22, 3, 'Ketchup Zero', 'Ketchup senza zuccheri aggiunti, zero calorie', 'Hakustore', NULL, '2026-07-24 00:00:00');
INSERT INTO public.prodotto (id_prodotto, id_categoria, nome, descrizione, marca, immagine, data_creazione) VALUES (23, 3, 'Salsa Teriyaki', 'Salsa teriyaki premium blend, senza conservanti aggiunti, gluten free', 'Hakustore', NULL, '2026-07-24 00:00:00');


--
-- Data for Name: variante_prodotto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (7, 1, 'Brownie', '500gr', 17.0, 5, NULL, 'variante_7.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (8, 1, 'Brownie', '1KG', 29.9, 4, NULL, 'variante_8.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (9, 1, 'Brownie', '2KG', 54.9, 5, NULL, 'variante_9.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (22, 1, 'Vaniglia', '500gr', 17.0, 10, NULL, 'variante_22.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (23, 1, 'Vaniglia', '1KG', 29.9, 10, NULL, 'variante_23.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (24, 1, 'Vaniglia', '2KG', 54.9, 10, NULL, 'variante_24.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (86, 1, 'Naturale', '500gr', 17.0, 10, NULL, 'variante_86.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (87, 1, 'Naturale', '1KG', 29.9, 10, NULL, 'variante_87.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (88, 1, 'Naturale', '2KG', 54.9, 10, NULL, 'variante_88.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (2, 2, 'Banana', '200ml', 30.0, 5, NULL, 'variante_2.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (13, 3, 'Cola', '500gr', 14.9, 10, NULL, 'variante_13.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (14, 3, 'Cola', '1000gr', 24.9, 10, NULL, 'variante_14.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (15, 3, 'Cola', '2000gr', 44.9, 10, NULL, 'variante_15.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (16, 3, 'Lime', '500gr', 14.9, 10, NULL, 'variante_16.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (17, 3, 'Lime', '1000gr', 24.9, 10, NULL, 'variante_17.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (18, 3, 'Lime', '2000gr', 44.9, 10, NULL, 'variante_18.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (19, 3, 'Naturale', '500gr', 14.9, 10, NULL, 'variante_19.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (20, 3, 'Naturale', '1000gr', 24.9, 10, NULL, 'variante_20.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (21, 3, 'Naturale', '2000gr', 44.9, 10, NULL, 'variante_21.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (89, 4, 'Vitamina D3', '90 compresse', 12.9, 10, NULL, 'variante_89.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (90, 4, 'Vitamina K2', '60 compresse', 14.9, 10, NULL, 'variante_90.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (91, 4, 'Multivitaminico', '90 compresse', 16.9, 10, NULL, 'variante_91.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (25, 6, 'Oreo', '500gr', 6.9, 10, NULL, 'variante_25.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (26, 6, 'Oreo', '1000gr', 11.9, 10, NULL, 'variante_26.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (27, 6, 'Oreo', '2000gr', 19.9, 10, NULL, 'variante_27.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (28, 6, 'Neutro', '500gr', 6.9, 10, NULL, 'variante_28.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (29, 6, 'Neutro', '1000gr', 11.9, 10, NULL, 'variante_29.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (30, 6, 'Neutro', '2000gr', 19.9, 10, NULL, 'variante_30.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (31, 6, 'Biscotto e Crema', '500gr', 6.9, 10, NULL, 'variante_31.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (32, 6, 'Biscotto e Crema', '1000gr', 11.9, 10, NULL, 'variante_32.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (33, 6, 'Biscotto e Crema', '2000gr', 19.9, 10, NULL, 'variante_33.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (34, 6, 'Vaniglia', '500gr', 6.9, 10, NULL, 'variante_34.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (35, 6, 'Vaniglia', '1000gr', 11.9, 10, NULL, 'variante_35.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (36, 6, 'Vaniglia', '2000gr', 19.9, 10, NULL, 'variante_36.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (37, 6, 'Cioccolato', '500gr', 6.9, 10, NULL, 'variante_37.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (38, 6, 'Cioccolato', '1000gr', 11.9, 10, NULL, 'variante_38.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (39, 6, 'Cioccolato', '2000gr', 19.9, 10, NULL, 'variante_39.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (40, 7, 'Cioccolato', '500gr', 19.9, 10, NULL, 'variante_40.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (41, 7, 'Cioccolato', '1000gr', 34.9, 10, NULL, 'variante_41.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (42, 7, 'Cioccolato', '2000gr', 64.9, 10, NULL, 'variante_42.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (43, 7, 'Vaniglia', '500gr', 19.9, 10, NULL, 'variante_43.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (44, 7, 'Vaniglia', '1000gr', 34.9, 10, NULL, 'variante_44.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (45, 7, 'Vaniglia', '2000gr', 64.9, 10, NULL, 'variante_45.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (46, 7, 'Brownie', '500gr', 19.9, 10, NULL, 'variante_46.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (47, 7, 'Brownie', '1000gr', 34.9, 10, NULL, 'variante_47.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (48, 7, 'Brownie', '2000gr', 64.9, 10, NULL, 'variante_48.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (49, 8, 'Vaniglia', '500gr', 18.9, 10, NULL, 'variante_49.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (50, 8, 'Vaniglia', '1000gr', 32.9, 10, NULL, 'variante_50.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (51, 8, 'Vaniglia', '2000gr', 59.9, 10, NULL, 'variante_51.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (52, 8, 'Cioccolato', '500gr', 18.9, 10, NULL, 'variante_52.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (53, 8, 'Cioccolato', '1000gr', 32.9, 10, NULL, 'variante_53.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (54, 8, 'Cioccolato', '2000gr', 59.9, 10, NULL, 'variante_54.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (55, 8, 'Brownie', '500gr', 18.9, 10, NULL, 'variante_55.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (56, 8, 'Brownie', '1000gr', 32.9, 10, NULL, 'variante_56.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (57, 8, 'Brownie', '2000gr', 59.9, 10, NULL, 'variante_57.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (58, 9, 'Brownie', '500gr', 21.9, 10, NULL, NULL, NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (59, 9, 'Brownie', '1000gr', 37.9, 10, NULL, NULL, NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (60, 9, 'Brownie', '2000gr', 69.9, 10, NULL, NULL, NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (61, 9, 'Cioccolato', '500gr', 21.9, 10, NULL, 'variante_61.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (62, 9, 'Cioccolato', '1000gr', 37.9, 10, NULL, 'variante_62.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (63, 9, 'Cioccolato', '2000gr', 69.9, 10, NULL, 'variante_63.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (64, 9, 'Vaniglia', '500gr', 21.9, 10, NULL, 'variante_64.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (65, 9, 'Vaniglia', '1000gr', 37.9, 10, NULL, 'variante_65.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (66, 9, 'Vaniglia', '2000gr', 69.9, 10, NULL, 'variante_66.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (67, 10, 'Pistacchio Fondente', 'Box da 12', 28.9, 10, NULL, 'variante_67.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (68, 10, 'Cioccolato Bianco', 'Box da 12', 28.9, 10, NULL, 'variante_68.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (72, 14, NULL, '60 compresse', 9.9, 10, NULL, 'variante_72.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (73, 15, NULL, '60 capsule', 13.9, 10, NULL, 'variante_73.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (74, 16, NULL, '60 compresse', 11.9, 10, NULL, 'variante_74.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (75, 17, NULL, '350ml', 6.9, 10, NULL, 'variante_75.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (76, 18, NULL, NULL, 12.9, 10, 'Grigio', 'variante_76.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (77, 18, NULL, NULL, 12.9, 10, 'Crema', 'variante_77.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (78, 18, NULL, NULL, 12.9, 10, 'Nero', 'variante_78.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (79, 19, NULL, NULL, 9.9, 10, 'Verde', 'variante_79.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (80, 19, NULL, NULL, 9.9, 10, 'Rosa', 'variante_80.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (81, 19, NULL, NULL, 9.9, 10, 'Nero', 'variante_81.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (82, 19, NULL, NULL, 9.9, 10, 'Celeste', 'variante_82.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (83, 19, NULL, NULL, 9.9, 10, 'Trasparente', 'variante_83.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (84, 20, NULL, NULL, 16.9, 10, 'Nero', 'variante_84.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (85, 21, NULL, NULL, 39.9, 10, 'Nero', 'variante_85.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (92, 22, NULL, '350ml', 6.9, 10, NULL, 'variante_92.jpeg', NULL);
INSERT INTO public.variante_prodotto (id_variante, id_prodotto, gusto, formato, prezzo, quantita_disponibile, colore, immagine, data_rifornimento) VALUES (93, 23, NULL, '350ml', 6.9, 10, NULL, 'variante_93.jpeg', NULL);


--
-- Data for Name: dettaglio_carrello; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.dettaglio_carrello (id_carrello, id_dettaglio, id_variante, quantita) VALUES (2, 12, 1, 1);


--
-- Data for Name: ordine; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.ordine (id_ordine, id_utente, data_ordine, totale_prodotti, valore_sconto, totale_pagato, codice_coupon_usato, stato, spedizione_via, spedizione_citta, spedizione_cap, spedizione_provincia, spedizione_nazione, metodo_pagamento, stato_pagamento) VALUES (1, 1, '2026-07-13 17:09:14.31948', 59.80, 5.98, 53.82, 'WELCOME10', 'SPEDITO', 'Via Roma 1', 'Milano', '20100', 'MI', 'Italia', 'CARTA', 'DA_PAGARE');
INSERT INTO public.ordine (id_ordine, id_utente, data_ordine, totale_prodotti, valore_sconto, totale_pagato, codice_coupon_usato, stato, spedizione_via, spedizione_citta, spedizione_cap, spedizione_provincia, spedizione_nazione, metodo_pagamento, stato_pagamento) VALUES (2, 2, '2026-07-13 17:19:37.828714', 29.90, 0.00, 29.90, NULL, 'SPEDITO', 'roma 123', 'roma', '70145', 'roma', 'Italia', 'CARTA', 'APPROVATO');
INSERT INTO public.ordine (id_ordine, id_utente, data_ordine, totale_prodotti, valore_sconto, totale_pagato, codice_coupon_usato, stato, spedizione_via, spedizione_citta, spedizione_cap, spedizione_provincia, spedizione_nazione, metodo_pagamento, stato_pagamento) VALUES (5, 2, '2026-07-14 15:02:48.864547', 75.00, 7.50, 67.50, 'WELCOME10', 'SPEDITO', 'roma 123', 'roma', '70145', 'roma', 'Italia', 'CARTA', 'DA_PAGARE');
INSERT INTO public.ordine (id_ordine, id_utente, data_ordine, totale_prodotti, valore_sconto, totale_pagato, codice_coupon_usato, stato, spedizione_via, spedizione_citta, spedizione_cap, spedizione_provincia, spedizione_nazione, metodo_pagamento, stato_pagamento) VALUES (4, 2, '2026-07-14 10:46:46.059869', 49.90, 30.00, 19.90, 'GERARD10', 'ELABORATO', 'roma 123', 'roma', '70145', 'roma', 'Italia', 'PAYPAL', 'APPROVATO');
INSERT INTO public.ordine (id_ordine, id_utente, data_ordine, totale_prodotti, valore_sconto, totale_pagato, codice_coupon_usato, stato, spedizione_via, spedizione_citta, spedizione_cap, spedizione_provincia, spedizione_nazione, metodo_pagamento, stato_pagamento) VALUES (3, 1, '2026-07-13 17:27:15.06716', 30.00, 30.00, 0.00, 'GERARD10', 'ANNULLATO', 'Via Roma 1', 'Milano', '20100', 'MI', 'Italia', 'CARTA', 'FALLITO');


--
-- Data for Name: dettaglio_ordine; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (1, 1, 1, 2, 29.90);
INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (2, 2, 1, 1, 29.90);
INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (3, 3, 2, 1, 30.00);
INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (4, 4, 3, 1, 20.00);
INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (5, 4, 1, 1, 29.90);
INSERT INTO public.dettaglio_ordine (id_dettaglio, id_ordine, id_variante, quantita, prezzo_unitario) VALUES (6, 5, 3, 2, 20.00);


--
-- Data for Name: indirizzo; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.indirizzo (id_indirizzo, id_utente, via, citta, cap, provincia, nazione) VALUES (1, 1, 'Via Roma 1', 'Milano', '20100', 'MI', 'Italia');
INSERT INTO public.indirizzo (id_indirizzo, id_utente, via, citta, cap, provincia, nazione) VALUES (2, 2, 'roma 123', 'roma', '70145', 'roma', 'Italia');


--
-- Data for Name: messaggi_sistema; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'auth.badcredentials', 'Email o password non validi.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'auth.forbidden', 'Non hai i permessi per eseguire questa operazione.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'auth.unauthorized', 'Devi effettuare il login per accedere a questa risorsa.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'auth.no.email', 'Inserisci l''email.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'auth.no.password', 'Inserisci la password.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.ntfnd', 'Utente non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.forbidden', 'Non puoi modificare i dati di un altro utente.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.email.exists', 'Esiste già un utente con questa email.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.email.invalid', 'L''indirizzo email non è valido.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.no.id', 'ID utente mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.no.nome', 'Inserisci il nome.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.no.cognome', 'Inserisci il cognome.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.no.email', 'Inserisci l''email.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.no.password', 'Inserisci la password.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'utente.password.short', 'La password deve contenere almeno 8 caratteri.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.ntfnd', 'Indirizzo non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.forbidden', 'Non puoi modificare un indirizzo che non ti appartiene.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.no.id', 'ID indirizzo mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.no.via', 'Inserisci la via.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.no.citta', 'Inserisci la città.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'indirizzo.no.cap', 'Inserisci il CAP.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'categoria.ntfnd', 'Categoria non trovata.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'categoria.nome.exist', 'Esiste già una categoria con questo nome.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'categoria.has.prodotti', 'Impossibile eliminare la categoria: sono presenti prodotti collegati.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'categoria.no.id', 'ID categoria mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'categoria.no.nome', 'Inserisci il nome della categoria.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.ntfnd', 'Prodotto non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.no.id', 'ID prodotto mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.no.categoria', 'Seleziona la categoria del prodotto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.no.nome', 'Inserisci il nome del prodotto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.no.marca', 'Inserisci la marca del prodotto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.ntfnd', 'Variante prodotto non trovata.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.stock.insufficient', 'Quantità non disponibile in magazzino.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.no.id', 'ID variante mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.no.prodotto', 'Seleziona il prodotto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.no.prezzo', 'Inserisci il prezzo.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.prezzo.invalid', 'Il prezzo non può essere negativo.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.quantita.invalid', 'La quantità disponibile non può essere negativa.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.ntfnd', 'Recensione non trovata.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.forbidden', 'Non puoi modificare una recensione che non è tua.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.no.id', 'ID recensione mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.no.prodotto', 'Seleziona il prodotto da recensire.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.no.voto', 'Inserisci un voto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'recensione.voto.invalid', 'Il voto deve essere compreso tra 1 e 5.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.ntfnd', 'Coupon non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.codice.exist', 'Esiste già un coupon con questo codice.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.not.active', 'Il coupon non è attivo.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.not.started', 'Il coupon non è ancora valido.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.expired', 'Il coupon è scaduto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.dates.invalid', 'La data di fine deve essere successiva alla data di inizio.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.id', 'ID coupon mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.codice', 'Inserisci il codice del coupon.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.tipologia', 'Seleziona il tipo di coupon (percentuale o fisso).');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.valore', 'Inserisci il valore dello sconto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.valore.invalid', 'Il valore dello sconto deve essere maggiore di zero.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.data.inizio', 'Inserisci la data di inizio validità.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'coupon.no.data.fine', 'Inserisci la data di fine validità.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'carrello.empty', 'Il carrello è vuoto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'carrello.no.variante', 'Seleziona una variante del prodotto.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'carrello.no.quantita', 'Inserisci la quantità.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'carrello.quantita.invalid', 'La quantità deve essere almeno 1.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'carrello.no.codice.coupon', 'Inserisci il codice del coupon.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'dettaglio.ntfnd', 'Articolo del carrello non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'ordine.ntfnd', 'Ordine non trovato.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'ordine.forbidden', 'Non puoi visualizzare un ordine che non ti appartiene.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'ordine.no.id', 'ID ordine mancante.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'ordine.no.indirizzo', 'Seleziona l''indirizzo di spedizione.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'ordine.no.metodo.pagamento', 'Seleziona il metodo di pagamento.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'prodotto.exists', 'Esiste già un prodotto con questo nome per questa marca.');
INSERT INTO public.messaggi_sistema (lang, code, messagio) VALUES ('IT', 'variante.exists', 'Esiste già una variante con questi stessi attributi per questo prodotto.');


--
-- Data for Name: recensione; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.recensione (id_recensione, id_prodotto, id_utente, voto, titolo, commento, data_recensione) VALUES (1, 1, 1, 5, 'Ottimo!', 'Prodotto eccellente, consigliato.', '2026-07-13 17:10:44.080699');
INSERT INTO public.recensione (id_recensione, id_prodotto, id_utente, voto, titolo, commento, data_recensione) VALUES (2, 3, 2, 5, 'Ottimo prodotto!!', 'Lo consiglio', '2026-07-14 10:44:36.402141');


--
-- Name: carrello_id_carrello_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.carrello_id_carrello_seq', 3, false);


--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.categoria_id_categoria_seq', 4, false);


--
-- Name: coupon_id_coupon_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.coupon_id_coupon_seq', 3, false);


--
-- Name: dettaglio_carrello_id_dettaglio_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.dettaglio_carrello_id_dettaglio_seq', 14, false);


--
-- Name: dettaglio_ordine_id_dettaglio_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.dettaglio_ordine_id_dettaglio_seq', 8, false);


--
-- Name: indirizzo_id_indirizzo_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.indirizzo_id_indirizzo_seq', 3, false);


--
-- Name: ordine_id_ordine_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.ordine_id_ordine_seq', 6, false);


--
-- Name: prodotto_id_prodotto_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.prodotto_id_prodotto_seq', 24, false);


--
-- Name: recensione_id_recensione_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.recensione_id_recensione_seq', 3, false);


--
-- Name: utente_id_utente_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.utente_id_utente_seq', 11, false);


--
-- Name: variante_prodotto_id_variante_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.variante_prodotto_id_variante_seq', 94, false);


--
-- PostgreSQL database dump complete
--

\unrestrict CjiFvM84FGfi75EpLJNzIYc7Qecimw0sG9Co8Ii4DEfz3hU7y1BG1Vt9cZDV7WX

