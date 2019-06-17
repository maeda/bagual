insert into companies (id, cod_empresa, nome_fantasia, responsavel_legal, email, website, created, modified) values (1, '08887571000100', 'Bla', 'Andherson Maeda', 'maeda.br@gmail.com', null, '2011-09-22 00:00:00', '2011-09-22 00:00:00')

insert into aliases (id, company_id, alias, analytics, root_redirect, created, modified) values (5, 1, 'bagu.al', 'UA-12204080-8', null, '2011-09-28 12:32:47', '2011-10-05 19:43:58')
insert into aliases (id, company_id, alias, analytics, root_redirect, created, modified) values (6, 1, 'kard.ec', 'UA-12204080-11', null, '2011-10-11 22:49:05', '2011-11-09 10:22:55')

insert into seeds (id, alias_id, seed, created, modified) values (5, 5, 6402, '2011-10-05 19:43:58', '2018-05-30 10:57:10')
insert into seeds (id, alias_id, seed, created, modified) values (6, 6, 681, '2011-10-12 13:51:35', '2018-05-23 21:57:40')

insert into urls (id, url_original, suspect, shortcut_count, created, modified) values (3613, 'http://google.com.br', 0, 52, '2012-02-12 20:12:22', '2012-04-08 19:41:27')
insert into urls (id, url_original, suspect, shortcut_count, created, modified) values (42640, 'http://mainiawa.weebly.com', 0, 0, '2017-07-25 22:26:41', '2017-07-25 22:26:41')
insert into urls (id, url_original, suspect, shortcut_count, created, modified) values (42641, 'http://204787org.ukit.me/', 1, 0, '2017-07-25 22:29:43', '2017-07-25 22:29:43')
insert into urls (id, url_original, suspect, shortcut_count, created, modified) values (43376, 'http://www.uetrk.com/aff_c?offer_id=9752&aff_id=13252&aff_sub=natasha', 0, 0, '2018-05-23 21:57:40', '2018-05-23 21:57:40')
insert into urls (id, url_original, suspect, shortcut_count, created, modified) values (43827, 'https://tinyurl.com/y4vyltp2', 0, 2, '2019-04-30 21:58:46', '2019-04-30 21:58:46')

insert into short_urls (id, alias_id, url_id, company_id, alias, shortcut, created, modified) values (5154, 5, 3613, 1,'bagu.al','6V','2012-04-08 19:41:27','2012-04-08 19:41:27')
insert into short_urls (id, alias_id, url_id, company_id, alias, shortcut, created, modified) values (42813, 5, 42640, 1,'bagu.al','1ue','2017-07-25 22:26:41','2017-07-25 22:26:41')
insert into short_urls (id, alias_id, url_id, company_id, alias, shortcut, created, modified) values (42814, 5, 42641, 1,'bagu.al','1uf','2017-07-25 22:29:43','2017-07-25 22:29:43')
insert into short_urls (id, alias_id, url_id, company_id, alias, shortcut, created, modified) values (43549, 5, 43376, 1,'kard.ec','aZ','2018-05-23 21:57:40','2018-05-23 21:57:40')
insert into short_urls (id, alias_id, url_id, company_id, alias, shortcut, created, modified, deleted) values (44002, 5, 43827, 1,'bagu.al','1Ma','2019-04-30 21:58:46','2019-04-30 21:58:46', '2019-06-17 19:59:00')

