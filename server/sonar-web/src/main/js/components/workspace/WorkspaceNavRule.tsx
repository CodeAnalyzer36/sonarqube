/*
 * SonarQube
 * Copyright (C) 2009-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import * as React from 'react';
import { RuleDescriptor } from './context';
import WorkspaceRuleTitle from './WorkspaceRuleTitle';
import ClearIcon from '../icons-components/ClearIcon';
import { ButtonIcon } from '../ui/buttons';

export interface Props {
  rule: RuleDescriptor;
  onClose: (ruleKey: string) => void;
  onOpen: (ruleKey: string) => void;
}

export default class WorkspaceNavRule extends React.PureComponent<Props> {
  handleCloseClick = () => {
    this.props.onClose(this.props.rule.key);
  };

  handleNameClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    event.currentTarget.blur();
    this.props.onOpen(this.props.rule.key);
  };

  render() {
    const { rule } = this.props;

    return (
      <li className="workspace-nav-item">
        <a className="workspace-nav-item-link" href="#" onClick={this.handleNameClick}>
          <WorkspaceRuleTitle rule={rule} />
        </a>
        <ButtonIcon
          className="js-close workspace-header-icon button-small little-spacer-left"
          color="#fff"
          onClick={this.handleCloseClick}>
          <ClearIcon fill={undefined} size={12} />
        </ButtonIcon>
      </li>
    );
  }
}
