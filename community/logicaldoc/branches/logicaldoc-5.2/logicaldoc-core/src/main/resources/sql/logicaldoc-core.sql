create table ld_bookmark (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_userid bigint not null, ld_docid bigint not null, ld_title varchar(255) not null, ld_description varchar(4000), ld_position int not null, ld_filetype varchar(40), primary key (ld_id));
create table ld_document (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_immutable int not null, ld_customid varchar(700), ld_title varchar(255), ld_version varchar(10), ld_fileversion varchar(10), ld_date timestamp, ld_creation timestamp not null, ld_publisher varchar(255), ld_publisherid bigint not null, ld_creator varchar(255), ld_creatorid bigint not null, ld_status int, ld_type varchar(255), ld_lockuserid bigint, ld_source varchar(4000), ld_sourceauthor varchar(255), ld_sourcedate timestamp, ld_sourceid varchar(1000), ld_sourcetype varchar(255), ld_object varchar(1000), ld_coverage varchar(255), ld_language varchar(10), ld_filename varchar(255), ld_filesize bigint, ld_indexed int not null, ld_signed int not null, ld_digest varchar(255), ld_recipient varchar(1000), ld_folderid bigint, ld_templateid bigint, ld_exportstatus int not null, ld_exportid bigint, ld_exportname varchar(255), ld_exportversion varchar(10), ld_docref bigint, ld_deleteuserid bigint, primary key (ld_id));
create table ld_document_ext (ld_docid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp, ld_name varchar(255) not null, primary key (ld_docid, ld_name));
create table ld_dcomment (ld_threadid bigint not null, ld_replyto int, ld_replypath varchar(255), ld_userid bigint not null, ld_username varchar(255), ld_date timestamp, ld_subject varchar(255), ld_body varchar(4000), ld_deleted int not null, ld_id int not null, primary key (ld_threadid, ld_id));
create table ld_dthread (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint not null, ld_creation timestamp, ld_creatorid bigint not null, ld_creatorname varchar(255), ld_lastpost timestamp, ld_subject varchar(255), ld_replies int, ld_views int, primary key (ld_id));
create table ld_generic (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_type varchar(255) not null, ld_subtype varchar(255) not null, ld_string1 varchar(4000), ld_string2 varchar(4000), ld_integer1 int, ld_integer2 int, ld_double1 float, ld_double2 float, ld_date1 timestamp, ld_date2 timestamp, primary key (ld_id));
create table ld_generic_ext (ld_genid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp, ld_name varchar(255) not null, primary key (ld_genid, ld_name));
create table ld_group (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_name varchar(255) not null, ld_description varchar(255), ld_type int not null, primary key (ld_id));
create table ld_group_ext (ld_groupid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp, ld_name varchar(255) not null, primary key (ld_groupid, ld_name));
create table ld_history (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint, ld_folderid bigint not null, ld_userid bigint, ld_date timestamp, ld_username varchar(255), ld_event varchar(255), ld_comment varchar(4000), ld_version varchar(10), ld_title varchar(255), ld_path varchar(4000), ld_notified int not null, ld_sessionid varchar(255), ld_new int, ld_filename varchar(255), primary key (ld_id));
create table ld_tag (ld_docid bigint not null, ld_tag varchar(255));
create table ld_link (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_type varchar(255) not null, ld_docid1 bigint, ld_docid2 bigint, primary key (ld_id));
create table ld_menu (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_text varchar(255), ld_parentid bigint not null, ld_securityref bigint, ld_sort int, ld_icon varchar(255), ld_type int not null, ld_ref varchar(255), ld_size bigint, ld_description varchar(4000), primary key (ld_id));
create table ld_menugroup (ld_menuid bigint not null, ld_groupid bigint not null, ld_write int not null, ld_addchild int not null, ld_managesecurity int not null, ld_manageimmutability int not null, ld_delete int not null, ld_rename int not null, ld_bulkimport int not null, ld_bulkexport int not null, ld_sign int not null, ld_archive int not null, ld_workflow int not null, primary key (ld_menuid, ld_groupid, ld_write, ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow));
create table ld_recipient (ld_messageid bigint not null, ld_name varchar(255) not null, ld_address varchar(255) not null, ld_mode varchar(255) not null, ld_type int not null, primary key (ld_messageid, ld_name, ld_address, ld_mode, ld_type));
create table ld_systemmessage (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_author varchar(255), ld_messagetext varchar(2000), ld_subject varchar(255), ld_sentdate timestamp not null, ld_datescope int, ld_prio int, ld_confirmation int, ld_red int not null, ld_lastnotified timestamp, ld_status int not null, ld_trials int, ld_type int not null, primary key (ld_id));
create table ld_template (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_name varchar(255) not null, ld_description varchar(2000), primary key (ld_id));
create table ld_template_ext (ld_templateid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp, ld_name varchar(255) not null, primary key (ld_templateid, ld_name));
create table ld_ticket (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_ticketid varchar(255) not null, ld_docid bigint not null, ld_userid bigint not null, primary key (ld_id));
create table ld_user (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_enabled int not null, ld_username varchar(255) not null, ld_password varchar(255), ld_name varchar(255), ld_firstname varchar(255), ld_street varchar(255), ld_postalcode varchar(255), ld_city varchar(255), ld_country varchar(255), ld_state varchar(255), ld_language varchar(10), ld_email varchar(255), ld_telephone varchar(255), ld_telephone2 varchar(255), ld_type int not null, ld_passwordchanged timestamp, ld_passwordexpires int not null, ld_source int not null, primary key (ld_id));
create table ld_user_history (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_userid bigint, ld_date timestamp, ld_username varchar(255), ld_event varchar(255), ld_comment varchar(4000), ld_notified int not null, ld_sessionid varchar(255), ld_new int, ld_filename varchar(255), primary key (ld_id));
create table ld_userdoc (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint not null, ld_userid bigint not null, ld_date timestamp, primary key (ld_id));
create table ld_usergroup (ld_groupid bigint not null, ld_userid bigint not null, primary key (ld_groupid, ld_userid));
create table ld_version (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_immutable int not null, ld_customid varchar(700), ld_title varchar(255), ld_version varchar(10), ld_fileversion varchar(10), ld_date timestamp, ld_creation timestamp, ld_publisher varchar(255), ld_publisherid bigint not null,  ld_creator varchar(255), ld_creatorid bigint not null, ld_status int, ld_type varchar(255), ld_lockuserid bigint, ld_source varchar(4000), ld_sourceauthor varchar(255), ld_sourcedate timestamp, ld_sourceid varchar(1000), ld_sourcetype varchar(255), ld_object varchar(1000), ld_coverage varchar(255), ld_language varchar(10), ld_filename varchar(255), ld_filesize bigint, ld_indexed int not null, ld_signed int not null, ld_digest varchar(255), ld_recipient varchar(1000), ld_folderid bigint, ld_foldername varchar(1000), ld_templateid bigint, ld_templatename varchar(1000), ld_tgs varchar(1000), ld_username varchar(255), ld_userid bigint, ld_versiondate timestamp, ld_comment varchar(1000),ld_event varchar(255), ld_documentid bigint not null, ld_exportstatus int not null, ld_exportid bigint, ld_exportname varchar(255), ld_exportversion varchar(10), ld_deleteuserid bigint, primary key (ld_id));
create table ld_version_ext (ld_versionid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp, ld_name varchar(255) not null, primary key (ld_versionid, ld_name));

alter table ld_document add constraint FK75ED9C0276C86307 foreign key (ld_templateid) references ld_template(ld_id);
alter table ld_document add constraint FK75ED9C027C565C60 foreign key (ld_folderid) references ld_menu(ld_id);
alter table ld_document_ext add constraint FK4E0884647C693DFD foreign key (ld_docid) references ld_document(ld_id);
alter table ld_generic_ext add constraint FK913AF772CF8376C7 foreign key (ld_genid) references ld_generic(ld_id);
alter table ld_group_ext add constraint FKB728EA5A76F11EA1 foreign key (ld_groupid) references ld_group(ld_id);
alter table ld_tag add constraint FK55BBDA227C693DFD foreign key (ld_docid) references ld_document(ld_id);
alter table ld_link add constraint FK1330661CADD6217 foreign key (ld_docid2) references ld_document(ld_id);
alter table ld_link add constraint FK1330661CADD6216 foreign key (ld_docid1) references ld_document(ld_id);
alter table ld_menugroup add constraint FKB4F7F679AA456AD1 foreign key (ld_menuid) references ld_menu(ld_id);
alter table ld_usergroup add constraint FK2435438DB8B12CA9 foreign key (ld_userid) references ld_user(ld_id);
alter table ld_usergroup add constraint FK2435438D76F11EA1 foreign key (ld_groupid) references ld_group(ld_id);
alter table ld_version add constraint FK9B3BD9118A053CE foreign key (ld_documentid) references ld_document(ld_id);
alter table ld_version_ext add constraint FK78C3A1F3B90495EE foreign key (ld_versionid) references ld_version(ld_id);
alter table ld_dcomment add constraint FKF2C40628DBB5BF4 foreign key (ld_threadid) references ld_dthread(ld_id);
alter table ld_template_ext add constraint FK6BABB84376C86307 foreign key (ld_templateid) references ld_template(ld_id);
alter table ld_recipient add constraint FK406A04126621DEBE foreign key (ld_messageid) references ld_systemmessage(ld_id);

alter table ld_ticket add constraint FK_TICKET_DOC foreign key (ld_docid) references ld_document(ld_id) on delete cascade;
alter table ld_ticket add constraint FK_TICKET_USER foreign key (ld_userid) references ld_user(ld_id) on delete cascade;
alter table ld_menugroup add constraint FK_MENUGROUP_GROUP foreign key (ld_groupid) references ld_group(ld_id) on delete cascade;
alter table ld_userdoc add constraint FK_USERDOC_DOC foreign key (ld_docid) references ld_document(ld_id) on delete cascade;
alter table ld_userdoc add constraint FK_USERDOC_USER foreign key (ld_userid) references ld_user(ld_id) on delete cascade;
alter table ld_menu add constraint FK_MENU_PARENT foreign key (ld_parentid) references ld_menu(ld_id) on delete cascade;

--On MySQL AK fields cannot be larger than
create unique index  AK_DOCUMENT on ld_document (ld_customid);
create unique index  AK_USER on ld_user (ld_username);
create unique index  AK_GROUP on ld_group (ld_name);  
create unique index  AK_TICKET on ld_ticket (ld_ticketid);
create unique index  AK_USERGROUP on ld_usergroup (ld_groupid, ld_userid);
create unique index  AK_LINK on ld_link (ld_docid1, ld_docid2, ld_type);
create unique index  AK_TEMPLATE on ld_template (ld_name);
create unique index  AK_GENERIC on ld_generic (ld_type, ld_subtype);
create unique index  AK_VERSION on ld_version (ld_documentid, ld_version);
alter table ld_version add constraint FK_VERSION_USER foreign key (ld_userid) references ld_user(ld_id);


insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (1,CURRENT_TIMESTAMP,0,'menu.home',1,1,'home.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (2,CURRENT_TIMESTAMP,0,'menu.admin',1,1,'administration.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (4,CURRENT_TIMESTAMP,0,'menu.personal',1,3,'personal.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (5,CURRENT_TIMESTAMP,0,'menu.documents',1,4,'documents.png',1,'document/browse',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (9,CURRENT_TIMESTAMP,0,'admin.security',2,3,'password.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (6,CURRENT_TIMESTAMP,0,'menu.user',9,5,'user.png',1,'admin/users',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (7,CURRENT_TIMESTAMP,0,'menu.group',9,10,'group.png',1,'admin/groups',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (-1,CURRENT_TIMESTAMP,0,'admin.security',9,15,'password.png',1,'admin/security',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (14,CURRENT_TIMESTAMP,0,'task.tasks',2,10,'task_scheduler.png',1,'admin/tasks',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (-2,CURRENT_TIMESTAMP,0,'system',2,1,'system.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (3,CURRENT_TIMESTAMP,0,'clients',2,2,'connect.png',1,'admin/clients',0);


insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (17,CURRENT_TIMESTAMP,0,'directory',-2,5,'folders.png',1,'admin/folders',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (8,CURRENT_TIMESTAMP,0,'menu.logging',-2,10,'logging.png',1,'admin/logs',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (25,CURRENT_TIMESTAMP,0,'menu.searchengine',-2,15,'search.png',1,'admin/searchEngine',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (23,CURRENT_TIMESTAMP,0,'smtp',-2,25,'mail_preferences.png',1,'admin/smtp',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (11,CURRENT_TIMESTAMP,0,'menu.gui',-2,30,'gui.png',1,'admin/gui',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (10,CURRENT_TIMESTAMP,0,'parameters',-2,35,'parameters.png',1,'admin/parameters',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (13,CURRENT_TIMESTAMP,0,'menu.messages',4,1,'message.png',1,'communication/messages',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (16,CURRENT_TIMESTAMP,0,'menu.changepassword',4,3,'password.png',1,'settings/password',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (19,CURRENT_TIMESTAMP,0,'menu.editme',4,4,'user.png',1,'settings/personalData',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (21,CURRENT_TIMESTAMP,0,'templates',2,4,'template.png',1,'admin/templates',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (18,CURRENT_TIMESTAMP,0,'searches',1,6,'search.png',1,null,0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (26,CURRENT_TIMESTAMP,0,'tags',18,5,'tags.png',1,'search/tags',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (15,CURRENT_TIMESTAMP,0,'search.advanced',18,5,'search.png',1,'search/advancedSearch',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (-80,CURRENT_TIMESTAMP,0,'menu.bookmarks',4,-80,'bookmarks.png',1,'settings/bookmarks',0);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_sort,ld_icon,ld_type,ld_ref,ld_size)
values     (22,CURRENT_TIMESTAMP,0,'menu.trash',4,25,'trash.png',1,'settings/trash',0);


insert into ld_group
values     (1,CURRENT_TIMESTAMP,0,'admin','Group of admins',0);

insert into ld_group
values     (2,CURRENT_TIMESTAMP,0,'author','Group of authors',0);

insert into ld_group
values     (3,CURRENT_TIMESTAMP,0,'guest','Group of guest',0);

insert into ld_user
           (ld_id,ld_lastmodified,ld_deleted,ld_enabled,ld_username,ld_password,ld_name,ld_firstname,ld_street,ld_postalcode,ld_city,ld_country,ld_language,ld_email,ld_telephone,ld_telephone2,ld_type,ld_passwordchanged,ld_passwordexpires,ld_source)
values     (1,CURRENT_TIMESTAMP,0,1,'admin','d033e22ae348aeb566fc214aec3585c4da997','Admin','Admin','','','','','en','admin@admin.net','','',0,null,0,0);
insert into ld_group
values     (-1,CURRENT_TIMESTAMP,0,'_user_1','',1);
insert into ld_usergroup
values (1,1);
insert into ld_usergroup
values (-1,1);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (1,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (4,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (5,2,1,1,0,0,1,1,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (13,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (16,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (19,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (23,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (26,2,1,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (18,2,1,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (15,2,1,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (-80,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (22,2,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (1,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (4,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (5,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (13,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (16,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (19,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (-80,3,0,0,0,0,0,0,0,0,0,0,0);

insert into ld_menugroup(ld_menuid, ld_groupid, ld_write , ld_addchild, ld_managesecurity, ld_manageimmutability, ld_delete, ld_rename, ld_bulkimport, ld_bulkexport, ld_sign, ld_archive, ld_workflow)
values     (22,3,0,0,0,0,0,0,0,0,0,0,0);