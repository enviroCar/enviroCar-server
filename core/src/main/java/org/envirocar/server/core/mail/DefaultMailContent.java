/*
 * Copyright (C) 2013-2021 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.core.mail;

import java.util.Objects;

import com.google.common.net.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DefaultMailContent implements MailContent {

    private final String content;
    private final MediaType contentType;

    public DefaultMailContent(String content, MediaType contentType) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    public static DefaultMailContent html(String content) {
        return new DefaultMailContent(content, MediaType.HTML_UTF_8);
    }

    public static DefaultMailContent plain(String content) {
        return new DefaultMailContent(content, MediaType.PLAIN_TEXT_UTF_8);
    }

}
